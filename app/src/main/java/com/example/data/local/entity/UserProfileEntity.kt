package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey
    val id: Int = 1,
    val username: String = "Arc Warrior",
    val avatarEmoji: String = "❄️",
    val level: Int = 1,
    val totalXp: Int = 0,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val completedGoalsCount: Int = 0,
    val completedArcsCount: Int = 0,
    val goalRemindersEnabled: Boolean = true,
    val dailyCheckInEnabled: Boolean = true,
    val streakProtectionEnabled: Boolean = true,
    val morningMotivationEnabled: Boolean = true,
    val eveningReflectionEnabled: Boolean = true,
    val isOnboardingCompleted: Boolean = false
)
