package com.riqsphere.myapplication.watchlist.room

import androidx.lifecycle.LiveData

class WatchlistAnimeRepository(private val watchlistAnimeDao: WatchlistAnimeDao){
    val allWatchlistAnime: LiveData<List<WatchlistAnime>> = watchlistAnimeDao.getUnorderedWatchlistAnime()

    suspend fun insert(watchlistAnime: WatchlistAnime) {
        watchlistAnimeDao.insert(watchlistAnime)
    }

    suspend fun updateEpisodesOut(episodes: Int, id: Int) {
        watchlistAnimeDao.updateEpisodesOut(episodes, id)
    }

    suspend fun deleteAll() {
        watchlistAnimeDao.deleteAll()
    }
}