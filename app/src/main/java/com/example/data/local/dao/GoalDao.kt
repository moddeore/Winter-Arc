package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.GoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    @Query("SELECT * FROM goals WHERE isArchived = 0 ORDER BY isPaused ASC, id ASC")
    fun getAllActiveGoals(): Flow<List<GoalEntity>>

    @Query("SELECT * FROM goals WHERE id = :id")
    fun getGoalByIdFlow(id: Long): Flow<GoalEntity?>

    @Query("SELECT * FROM goals WHERE id = :id")
    suspend fun getGoalById(id: Long): GoalEntity?

    @Query("SELECT * FROM goals WHERE categoryId = :categoryId AND isArchived = 0")
    fun getGoalsByCategory(categoryId: Long): Flow<List<GoalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: GoalEntity): Long

    @Update
    suspend fun updateGoal(goal: GoalEntity)

    @Query("UPDATE goals SET isPaused = :isPaused WHERE id = :id")
    suspend fun setGoalPaused(id: Long, isPaused: Boolean)

    @Query("UPDATE goals SET isArchived = 1 WHERE id = :id")
    suspend fun archiveGoal(id: Long)

    @Delete
    suspend fun deleteGoal(goal: GoalEntity)

    @Query("DELETE FROM goals WHERE id = :id")
    suspend fun deleteGoalById(id: Long)
}
