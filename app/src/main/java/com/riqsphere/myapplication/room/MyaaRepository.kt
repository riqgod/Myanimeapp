package com.riqsphere.myapplication.room

import androidx.lifecycle.LiveData
import com.riqsphere.myapplication.model.watchlist.WatchlistAnime

class MyaaRepository(private val watchlistAnimeDao: WatchlistAnimeDao, private val recommendationDao: RecommendationDao){
    val allWatchlistAnime: LiveData<List<WatchlistAnime>> = watchlistAnimeDao.getUnorderedWatchlistAnime()

    suspend fun insert(watchlistAnime: WatchlistAnime) {
        watchlistAnimeDao.insert(watchlistAnime)
    }

    suspend fun updateEpisodesWatched(id: Int, episodesWatched: ArrayList<Int>) = watchlistAnimeDao.updateEpisodesWatched(id, episodesWatched)

    suspend fun updateEpisodesOut(id: Int, episodesOut: Int) = watchlistAnimeDao.updateEpisodesOut(id, episodesOut)

    suspend fun deleteAll() {
        watchlistAnimeDao.deleteAll()
    }
}