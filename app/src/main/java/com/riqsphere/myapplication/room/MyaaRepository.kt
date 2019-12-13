package com.riqsphere.myapplication.room

import androidx.lifecycle.LiveData
import com.riqsphere.myapplication.model.recommendations.Recommendation
import com.riqsphere.myapplication.model.watchlist.WatchlistAnime

class MyaaRepository(private val watchlistAnimeDao: WatchlistAnimeDao, private val recommendationDao: RecommendationDao){
    val allWatchlistAnime: LiveData<List<WatchlistAnime>> = watchlistAnimeDao.getWatchlistAnimes()
    val allRecommendation: LiveData<List<Recommendation>> = recommendationDao.getRecommendations()

    suspend fun insert(watchlistAnime: WatchlistAnime) = watchlistAnimeDao.insert(watchlistAnime)
    suspend fun insert(recommendation: Recommendation) = recommendationDao.insert(recommendation)

    suspend fun updateEpisodesWatched(id: Int, episodesWatched: ArrayList<Int>) = watchlistAnimeDao.updateEpisodesWatched(id, episodesWatched)

    suspend fun updateEpisodesOut(id: Int, episodesOut: Int) = watchlistAnimeDao.updateEpisodesOut(id, episodesOut)

    suspend fun deleteRecommendationsFrom(id: Int) = recommendationDao.delete(id)

    suspend fun deleteAll() {
        watchlistAnimeDao.deleteAll()
        recommendationDao.deleteAll()
    }
}