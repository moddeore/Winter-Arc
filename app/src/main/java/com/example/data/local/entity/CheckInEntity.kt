package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class DayMood(val emoji: String, val label: String) {
    SAD("😔", "Struggled"),
    NEUTRAL("😐", "Survived"),
    GOOD("🙂", "Solid"),
    FIRE("🔥", "Locked In")
}

@Entity(tableName = "check_ins")
data class CheckInEntity(
    @PrimaryKey
    val dateEpochDay: Long, // One check in per day
    val mood: DayMood = DayMood.FIRE,
    val reflection: String = "",
    val completedGoalsCount: Int = 0,
    val totalGoalsCount: Int = 0,
    val xpEarned: Int = 100,
    val timestamp: Long = System.currentTimeMillis()
)
