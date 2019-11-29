package com.riqsphere.myapplication.watchlist.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface watchlistAnimeDao {
    @Query("select * from watchlist_anime")
    fun getUnorderedWatchlistAnimes(): LiveData<List<watchlistAnime>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(watchlistAnime: watchlistAnime)

    @Query("delete from watchlist_anime")
    suspend fun deleteAll()
}