package com.riqsphere.myapplication.model.watchlist.room

import androidx.lifecycle.LiveData

class WatchlistAnimeRepository(private val watchlistAnimeDao: WatchlistAnimeDao){
    val allWatchlistAnime: LiveData<List<WatchlistAnime>> = watchlistAnimeDao.getUnorderedWatchlistAnime()

    suspend fun insert(watchlistAnime: WatchlistAnime) {
        watchlistAnimeDao.insert(watchlistAnime)
    }

    suspend fun updateEpisodesOut(id: Int, episodes: Int) {
        watchlistAnimeDao.updateEpisodesOut(id, episodes)
    }

    suspend fun deleteAll() {
        watchlistAnimeDao.deleteAll()
    }
}