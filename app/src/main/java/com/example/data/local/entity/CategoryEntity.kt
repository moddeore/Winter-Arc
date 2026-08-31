package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val icon: String, // emoji or icon name
    val colorHex: String,
    val isCustom: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
