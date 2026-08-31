package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal_entries")
data class JournalEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dateEpochDay: Long,
    val title: String,
    val content: String,
    val moodEmoji: String = "❄️",
    val dayNumber: Int = 1,
    val createdAt: Long = System.currentTimeMillis()
)
