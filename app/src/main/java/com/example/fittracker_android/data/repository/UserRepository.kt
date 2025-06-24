package com.example.fittracker_android.data.repository

import com.example.fittracker_android.data.local.dao.UserDao
import com.example.fittracker_android.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow
import java.security.MessageDigest
import java.util.UUID

/**
 * Repository Pattern - A clean way to manage data
 *
 * The repository acts as a middleman between your app and the database.
 * It handles all the business logic for data operations.
 *
 * Benefits:
 * - Your ViewModels don't need to know about the database
 * - Easy to test (you can mock the repository)
 * - Can add caching, API calls, etc. later without changing ViewModels
 */
class UserRepository(private val userDao: UserDao) {

    fun observeLoggedInUser(): Flow<UserEntity?> = userDao.observeLoggedInUser()

    suspend fun registerUser(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        age: Int?
    ): Result<UserEntity> {
        return try {
            val existingUser = userDao.getUserByEmail(email)
            if (existingUser != null) {
                return Result.failure(Exception("Email already registered"))
            }

            val newUser = UserEntity(
                id = UUID.randomUUID().toString(),
                username = "$firstName $lastName",
                email = email,
                passwordHash = hashPassword(password),
                age = age,
                isLoggedIn = true
            )

            userDao.logoutAllUsers()

            userDao.insertUser(newUser)

            Result.success(newUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginUser(email: String, password: String): Result<UserEntity> {
        return try {
            val user = userDao.getUserByEmail(email)
                ?: return Result.failure(Exception("User not found"))

            if (user.passwordHash != hashPassword(password)) {
                return Result.failure(Exception("Invalid password"))
            }

            userDao.logoutAllUsers()

            userDao.updateLoginStatus(user.id, true)

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        userDao.logoutAllUsers()
    }

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}