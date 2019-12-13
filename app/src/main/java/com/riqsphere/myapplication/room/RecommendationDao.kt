package com.riqsphere.myapplication.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.riqsphere.myapplication.model.recommendations.Recommendation

@Dao
interface RecommendationDao {
    @Query("select * from recommendation")
    fun getRecommendations(): LiveData<List<Recommendation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recommendation: Recommendation)

    @Query("delete from recommendation where from_id = :id")
    suspend fun delete(id: Int)

    @Query("delete from recommendation")
    suspend fun deleteAll()
}