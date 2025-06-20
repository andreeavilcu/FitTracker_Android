package com.example.fittracker_android.data.local

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Manager pentru User Preferences folosind SharedPreferences
 * Gestionează toate setările utilizatorului în aplicație
 */
class UserPreferencesManager(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME, Context.MODE_PRIVATE
    )

    // StateFlows pentru observarea schimbărilor în timp real
    private val _isDarkMode = MutableStateFlow(getBoolean(KEY_DARK_MODE, false))
    val isDarkMode: Flow<Boolean> = _isDarkMode.asStateFlow()

    private val _units = MutableStateFlow(getUnits())
    val units: Flow<Units> = _units.asStateFlow()

    private val _notificationsEnabled = MutableStateFlow(getBoolean(KEY_NOTIFICATIONS, true))
    val notificationsEnabled: Flow<Boolean> = _notificationsEnabled.asStateFlow()

    // ========== THEME SETTINGS ==========

    fun setDarkMode(enabled: Boolean) {
        putBoolean(KEY_DARK_MODE, enabled)
        _isDarkMode.value = enabled
    }

    fun getDarkMode(): Boolean = getBoolean(KEY_DARK_MODE, false)

    // ========== UNITS SETTINGS ==========

    fun setUnits(units: Units) {
        putString(KEY_UNITS, units.name)
        _units.value = units
    }

    fun getUnits(): Units {
        val unitsString = getString(KEY_UNITS, Units.METRIC.name)
        return try {
            Units.valueOf(unitsString)
        } catch (e: IllegalArgumentException) {
            Units.METRIC
        }
    }

    // ========== NOTIFICATION SETTINGS ==========

    fun setNotificationsEnabled(enabled: Boolean) {
        putBoolean(KEY_NOTIFICATIONS, enabled)
        _notificationsEnabled.value = enabled
    }

    fun getNotificationsEnabled(): Boolean = getBoolean(KEY_NOTIFICATIONS, true)

    fun setWorkoutReminders(enabled: Boolean) {
        putBoolean(KEY_WORKOUT_REMINDERS, enabled)
    }

    fun getWorkoutReminders(): Boolean = getBoolean(KEY_WORKOUT_REMINDERS, true)

    fun setReminderTime(time: String) {
        putString(KEY_REMINDER_TIME, time)
    }

    fun getReminderTime(): String = getString(KEY_REMINDER_TIME, "09:00")

    // ========== WORKOUT SETTINGS ==========

    fun setDefaultRestTime(seconds: Int) {
        putInt(KEY_DEFAULT_REST_TIME, seconds)
    }

    fun getDefaultRestTime(): Int = getInt(KEY_DEFAULT_REST_TIME, 60)

    fun setAutoNextSet(enabled: Boolean) {
        putBoolean(KEY_AUTO_NEXT_SET, enabled)
    }

    fun getAutoNextSet(): Boolean = getBoolean(KEY_AUTO_NEXT_SET, false)

    fun setKeepScreenOn(enabled: Boolean) {
        putBoolean(KEY_KEEP_SCREEN_ON, enabled)
    }

    fun getKeepScreenOn(): Boolean = getBoolean(KEY_KEEP_SCREEN_ON, true)

    // ========== USER PROFILE SETTINGS ==========

    fun setFirstTimeUser(isFirstTime: Boolean) {
        putBoolean(KEY_FIRST_TIME_USER, isFirstTime)
    }

    fun isFirstTimeUser(): Boolean = getBoolean(KEY_FIRST_TIME_USER, true)

    fun setLastSelectedUserId(userId: String?) {
        putString(KEY_LAST_USER_ID, userId)
    }

    fun getLastSelectedUserId(): String? = getNullableString(KEY_LAST_USER_ID, null)

    // ========== FITNESS GOALS SETTINGS ==========

    fun setWeeklyWorkoutGoal(goal: Int) {
        putInt(KEY_WEEKLY_WORKOUT_GOAL, goal)
    }

    fun getWeeklyWorkoutGoal(): Int = getInt(KEY_WEEKLY_WORKOUT_GOAL, 3)

    fun setDailyCalorieGoal(calories: Int) {
        putInt(KEY_DAILY_CALORIE_GOAL, calories)
    }

    fun getDailyCalorieGoal(): Int = getInt(KEY_DAILY_CALORIE_GOAL, 0)

    // ========== BACKUP & SYNC SETTINGS ==========

    fun setAutoBackup(enabled: Boolean) {
        putBoolean(KEY_AUTO_BACKUP, enabled)
    }

    fun getAutoBackup(): Boolean = getBoolean(KEY_AUTO_BACKUP, false)

    fun setLastBackupTime(timestamp: Long) {
        putLong(KEY_LAST_BACKUP_TIME, timestamp)
    }

    fun getLastBackupTime(): Long = getLong(KEY_LAST_BACKUP_TIME, 0L)

    // ========== HELPER METHODS ==========

    private fun putString(key: String, value: String?) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    private fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    private fun getNullableString(key: String, defaultValue: String?): String? {
        return sharedPreferences.getString(key, defaultValue)
    }

    private fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    private fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    private fun putInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    private fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    private fun putLong(key: String, value: Long) {
        sharedPreferences.edit().putLong(key, value).apply()
    }

    private fun getLong(key: String, defaultValue: Long): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

    fun clearAllPreferences() {
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        private const val PREFS_NAME = "fit_tracker_preferences"

        // Theme keys
        private const val KEY_DARK_MODE = "dark_mode"
        private const val KEY_UNITS = "units"

        // Notification keys
        private const val KEY_NOTIFICATIONS = "notifications_enabled"
        private const val KEY_WORKOUT_REMINDERS = "workout_reminders"
        private const val KEY_REMINDER_TIME = "reminder_time"

        // Workout keys
        private const val KEY_DEFAULT_REST_TIME = "default_rest_time"
        private const val KEY_AUTO_NEXT_SET = "auto_next_set"
        private const val KEY_KEEP_SCREEN_ON = "keep_screen_on"

        // User profile keys
        private const val KEY_FIRST_TIME_USER = "first_time_user"
        private const val KEY_LAST_USER_ID = "last_user_id"

        // Goals keys
        private const val KEY_WEEKLY_WORKOUT_GOAL = "weekly_workout_goal"
        private const val KEY_DAILY_CALORIE_GOAL = "daily_calorie_goal"

        // Backup keys
        private const val KEY_AUTO_BACKUP = "auto_backup"
        private const val KEY_LAST_BACKUP_TIME = "last_backup_time"
    }
}

// Enum pentru units (dacă nu există deja în modelele tale)
enum class Units(val displayName: String) {
    METRIC("Metric (kg, cm)"),
    IMPERIAL("Imperial (lbs, ft/in)")
}