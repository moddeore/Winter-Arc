package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.CheckInEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CheckInDao {
    @Query("SELECT * FROM check_ins ORDER BY dateEpochDay DESC")
    fun getAllCheckIns(): Flow<List<CheckInEntity>>

    @Query("SELECT * FROM check_ins WHERE dateEpochDay = :dateEpochDay LIMIT 1")
    fun getCheckInForDay(dateEpochDay: Long): Flow<CheckInEntity?>

    @Query("SELECT * FROM check_ins WHERE dateEpochDay = :dateEpochDay LIMIT 1")
    suspend fun getCheckInForDaySync(dateEpochDay: Long): CheckInEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheckIn(checkIn: CheckInEntity)
}
