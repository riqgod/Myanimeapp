package com.riqsphere.myapplication.model.watchlist.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WatchlistAnimeDao {
    @Query("select * from watchlist_anime")
    fun getUnorderedWatchlistAnime(): LiveData<List<WatchlistAnime>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(watchlistAnime: WatchlistAnime)

    @Query("update watchlist_anime set episodesWatched = :episodesWatched where id = :id")
    suspend fun updateEpisodesWatched(id: Int, episodesWatched: ArrayList<Int>)

    @Query("update watchlist_anime set episodesOut = :episodesOut where id = :id")
    suspend fun updateEpisodesOut(id: Int, episodesOut: Int)

    @Query("delete from watchlist_anime")
    suspend fun deleteAll()
}