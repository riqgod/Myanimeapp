package com.riqsphere.myapplication.watchlist.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WatchlistAnimeDao {
    @Query("select * from watchlist_anime")
    fun getUnorderedWatchlistAnime(): LiveData<List<WatchlistAnime>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(watchlistAnime: WatchlistAnime)

    @Query("update watchlist_anime set episodesWatched = :eps where id = :id")
    suspend fun updateEpisodes(eps: Array<Int>, id: Int)

    @Query("select episodesWatched from watchlist_anime where id = :id")
    suspend fun episodesWatched(id: Int): Array<Int>

    @Query("delete from watchlist_anime")
    suspend fun deleteAll()
}