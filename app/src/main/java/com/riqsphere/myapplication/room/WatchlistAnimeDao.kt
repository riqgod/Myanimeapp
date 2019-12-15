package com.riqsphere.myapplication.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.riqsphere.myapplication.model.watchlist.WatchlistAnime

@Dao
interface WatchlistAnimeDao {
    @Query("select * from watchlist_anime")
    fun getWatchlistAnimes(): LiveData<List<WatchlistAnime>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(watchlistAnime: WatchlistAnime)

    @Query("update watchlist_anime set episodesWatched = :episodesWatched where id = :id")
    suspend fun updateEpisodesWatched(id: Int, episodesWatched: String)

    @Query("update watchlist_anime set episodesOut = :episodesOut where id = :id")
    suspend fun updateEpisodesOut(id: Int, episodesOut: Int)

    @Query("delete from watchlist_anime where id = :id")
    suspend fun delete(id: Int)

    @Query("delete from watchlist_anime")
    suspend fun deleteAll()
}