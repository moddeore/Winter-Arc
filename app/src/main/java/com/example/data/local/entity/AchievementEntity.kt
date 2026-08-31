package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey
    val id: String, // e.g. "7_day_warrior", "frozen_mind", "locked_in", "the_summit", "perfect_week"
    val title: String,
    val description: String,
    val icon: String,
    val xpReward: Int,
    val isUnlocked: Boolean = false,
    val unlockedAt: Long? = null,
    val progressCurrent: Int = 0,
    val progressTarget: Int = 1
)
