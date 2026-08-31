package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class GoalType(val displayName: String) {
    DAILY_HABIT("Daily Habit"),
    WEEKLY_GOAL("Weekly Goal"),
    ONE_TIME_GOAL("One-Time Goal"),
    DURATION_GOAL("Duration Goal"),
    QUANTITY_GOAL("Quantity Goal"),
    LIMIT_GOAL("Limit Goal"),
    PROGRESS_GOAL("Progress Goal"),
    MILESTONE_GOAL("Milestone Goal")
}

enum class GoalDifficulty(val displayName: String, val xpReward: Int) {
    EASY("Easy", 10),
    MEDIUM("Medium", 25),
    HARD("Hard", 50),
    MAJOR("Major Milestone", 100)
}

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val arcId: Long = 1,
    val name: String,
    val description: String = "",
    val categoryId: Long,
    val categoryName: String,
    val categoryIcon: String,
    val goalType: GoalType = GoalType.DAILY_HABIT,
    val targetValue: Float = 1f,
    val targetUnit: String = "times", // "min", "hours", "pages", "problems", "%", "times"
    val targetFrequency: String = "Daily", // "Daily", "4x/week", "Weekly", "Total"
    val difficulty: GoalDifficulty = GoalDifficulty.MEDIUM,
    val isPaused: Boolean = false,
    val isArchived: Boolean = false,
    val isOneTimeCompleted: Boolean = false,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val totalXpEarned: Int = 0,
    val currentProgressValue: Float = 0f, // For persistent progress goals
    val createdAt: Long = System.currentTimeMillis()
)
