package com.riqsphere.myapplication.watchlist.room

import androidx.lifecycle.LiveData

class WatchlistAnimeRepository(private val watchlistAnimeDao: WatchlistAnimeDao){
    val allWatchlistAnime: LiveData<List<WatchlistAnime>> = watchlistAnimeDao.getUnorderedWatchlistAnime()

    suspend fun insert(watchlistAnime: WatchlistAnime) {
        watchlistAnimeDao.insert(watchlistAnime)
    }

    suspend fun addEpisodeWatched(episode: Int, id: Int) {
        val watched = watchlistAnimeDao.episodesWatched(id)
        val newWatched = watched.plus(episode)
        watchlistAnimeDao.updateEpisodes(newWatched, id)
    }

    suspend fun deleteAll() {
        watchlistAnimeDao.deleteAll()
    }
}