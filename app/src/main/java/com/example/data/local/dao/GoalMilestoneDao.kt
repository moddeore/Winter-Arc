package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.GoalMilestoneEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalMilestoneDao {
    @Query("SELECT * FROM goal_milestones WHERE goalId = :goalId ORDER BY orderIndex ASC")
    fun getMilestonesForGoal(goalId: Long): Flow<List<GoalMilestoneEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilestones(milestones: List<GoalMilestoneEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilestone(milestone: GoalMilestoneEntity): Long

    @Update
    suspend fun updateMilestone(milestone: GoalMilestoneEntity)

    @Query("UPDATE goal_milestones SET isCompleted = :isCompleted, completedAt = :completedAt WHERE id = :id")
    suspend fun setMilestoneCompleted(id: Long, isCompleted: Boolean, completedAt: Long?)

    @Query("DELETE FROM goal_milestones WHERE goalId = :goalId")
    suspend fun deleteMilestonesForGoal(goalId: Long)
}
