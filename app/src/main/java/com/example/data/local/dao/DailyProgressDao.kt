package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.DailyProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyProgressDao {
    @Query("SELECT * FROM daily_progress WHERE dateEpochDay = :dateEpochDay")
    fun getProgressForDay(dateEpochDay: Long): Flow<List<DailyProgressEntity>>

    @Query("SELECT * FROM daily_progress WHERE goalId = :goalId AND dateEpochDay = :dateEpochDay LIMIT 1")
    suspend fun getProgressForGoalAndDay(goalId: Long, dateEpochDay: Long): DailyProgressEntity?

    @Query("SELECT * FROM daily_progress WHERE goalId = :goalId ORDER BY dateEpochDay DESC")
    fun getHistoryForGoal(goalId: Long): Flow<List<DailyProgressEntity>>

    @Query("SELECT * FROM daily_progress ORDER BY dateEpochDay DESC")
    fun getAllProgressHistory(): Flow<List<DailyProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProgress(progress: DailyProgressEntity): Long

    @Query("DELETE FROM daily_progress WHERE goalId = :goalId")
    suspend fun deleteProgressForGoal(goalId: Long)
}
