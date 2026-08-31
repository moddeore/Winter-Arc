package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "winter_arcs")
data class WinterArcEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val motivation: String,
    val mainObjective: String,
    val startDate: Long, // epoch millis
    val endDate: Long, // epoch millis
    val durationDays: Int = 90,
    val baselineFitness: String = "",
    val baselineStudy: String = "",
    val baselineCoding: String = "",
    val baselineScreenTime: String = "",
    val baselineReflection: String = "",
    val finalReflection: String = "",
    val isActive: Boolean = true,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
