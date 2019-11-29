package com.riqsphere.myapplication.watchlist.persistence

import androidx.lifecycle.LiveData

class watchlistAnimeRepository(private val watchlistAnimeDao: watchlistAnimeDao){
    val allwatchlistAnime: LiveData<List<watchlistAnime>> = watchlistAnimeDao.getUnorderedWatchlistAnimes()

    suspend fun insert(watchlistAnime: watchlistAnime) {
        watchlistAnimeDao.insert(watchlistAnime)
    }
}