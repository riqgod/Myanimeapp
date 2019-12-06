package com.riqsphere.myapplication.watchlist.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WatchlistAnimeDao {
    @Query("select * from watchlist_anime")
    fun getUnorderedWatchlistAnime(): LiveData<List<WatchlistAnime>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(watchlistAnime: WatchlistAnime)

    @Query("update watchlist_anime set episodesOut = :eps where id = :id")
    suspend fun updateEpisodesOut(eps: Int, id: Int)

    @Query("delete from watchlist_anime")
    suspend fun deleteAll()
}