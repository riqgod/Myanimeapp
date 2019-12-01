package com.riqsphere.myapplication.watchlist.persistence

import androidx.lifecycle.LiveData

class watchlistAnimeRepository(private val watchlistAnimeDao: watchlistAnimeDao){
    val allwatchlistAnimes: LiveData<List<watchlistAnime>> = watchlistAnimeDao.getUnorderedWatchlistAnimes()

    suspend fun insertWatchlistAnime(watchlistAnime: watchlistAnime) {
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