package com.example.fittracker_android.data.local.dao

import androidx.room.*
import com.example.fittracker_android.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) for User operations
 *
 * This interface defines all the ways we can interact with the users table.
 * Room will generate the actual implementation code for us!
 *
 * @Dao - Marks this as a Data Access Object
 * suspend fun - These are coroutine functions that can be paused/resumed
 * Flow - Provides live updates when data changes
 */
@Dao
interface UserDao {

    // CREATE - Add a new user
    @Insert
    suspend fun insertUser(user: UserEntity)

    // READ - Get user data
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun getLoggedInUser(): UserEntity?

    // This returns Flow - it will automatically update when data changes!
    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
    fun observeLoggedInUser(): Flow<UserEntity?>

    // UPDATE - Modify existing user
    @Update
    suspend fun updateUser(user: UserEntity)

    // Custom update for login status
    @Query("UPDATE users SET isLoggedIn = :isLoggedIn WHERE id = :userId")
    suspend fun updateLoginStatus(userId: String, isLoggedIn: Boolean)

    // DELETE - Remove user
    @Delete
    suspend fun deleteUser(user: UserEntity)

    // Logout all users (for when switching accounts)
    @Query("UPDATE users SET isLoggedIn = 0")
    suspend fun logoutAllUsers()
}