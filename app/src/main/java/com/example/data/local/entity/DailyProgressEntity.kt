package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "daily_progress",
    foreignKeys = [
        ForeignKey(
            entity = GoalEntity::class,
            parentColumns = ["id"],
            childColumns = ["goalId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("goalId"),
        Index(value = ["goalId", "dateEpochDay"], unique = true)
    ]
)
data class DailyProgressEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val goalId: Long,
    val dateEpochDay: Long, // epoch day (System.currentTimeMillis() / (1000 * 60 * 60 * 24))
    val currentProgress: Float = 0f,
    val targetValue: Float = 1f,
    val isCompleted: Boolean = false,
    val xpEarned: Int = 0,
    val notes: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)
