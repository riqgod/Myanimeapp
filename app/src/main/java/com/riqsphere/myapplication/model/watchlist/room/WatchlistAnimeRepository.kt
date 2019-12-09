package com.riqsphere.myapplication.model.watchlist.room

import androidx.lifecycle.LiveData

class WatchlistAnimeRepository(private val watchlistAnimeDao: WatchlistAnimeDao){
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