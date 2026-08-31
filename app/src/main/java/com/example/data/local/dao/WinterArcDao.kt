package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.WinterArcEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WinterArcDao {
    @Query("SELECT * FROM winter_arcs WHERE isActive = 1 ORDER BY createdAt DESC LIMIT 1")
    fun getActiveArc(): Flow<WinterArcEntity?>

    @Query("SELECT * FROM winter_arcs ORDER BY createdAt DESC")
    fun getAllArcs(): Flow<List<WinterArcEntity>>

    @Query("SELECT * FROM winter_arcs WHERE id = :id")
    suspend fun getArcById(id: Long): WinterArcEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArc(arc: WinterArcEntity): Long

    @Update
    suspend fun updateArc(arc: WinterArcEntity)

    @Delete
    suspend fun deleteArc(arc: WinterArcEntity)
}
