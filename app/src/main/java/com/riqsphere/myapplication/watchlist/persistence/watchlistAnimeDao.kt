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

    @Query("update watchlist_anime set episodesWatched = :eps where id = :id")
    suspend fun updateEpisodes(eps: Array<Int>, id: Int)

    @Query("select episodesWatched from watchlist_anime where id = :id")
    suspend fun episodesWatched(id: Int): Array<Int>

    @Query("delete from watchlist_anime")
    suspend fun deleteAll()
}