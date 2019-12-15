package com.riqsphere.myapplication.room

import androidx.lifecycle.LiveData
import androidx.room.Transaction
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime
import com.riqsphere.myapplication.cache.JikanCacheHandler
import com.riqsphere.myapplication.model.recommendations.Recommendation
import com.riqsphere.myapplication.model.watchlist.WatchlistAnime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MyaaRepository(private val watchlistAnimeDao: WatchlistAnimeDao, private val recommendationDao: RecommendationDao){
    val allWatchlistAnime: LiveData<List<WatchlistAnime>> = watchlistAnimeDao.getWatchlistAnimes()
    val allRecommendation: LiveData<List<Recommendation>> = recommendationDao.getRecommendations()

    @Transaction
    suspend fun insert(watchlistAnime: WatchlistAnime) {
        watchlistAnimeDao.insert(watchlistAnime)
        recommendationDao.delete(watchlistAnime.id)
        val anime = watchlistAnime.toAnime()
        val recommend = withContext(Dispatchers.Unconfined) {
            JikanCacheHandler.getRecommendationPage(anime)
        }
        val recommendation = Recommendation.arrayListFrom(recommend.recommends)
        recommendation.forEach {
            insertRecWithCountZero(it)
            recommendationDao.addCount(it.id, it.count)
        }
    }

    private suspend fun insertRecWithCountZero(recommendation: Recommendation) {
        val temp = recommendation.count
        recommendation.count = 0
        recommendationDao.insert(recommendation)
        recommendation.count = temp
    }

    suspend fun updateEpisodesWatched(id: Int, episodesWatched: IntArray) {
        val episodesString = MyaaDatabase.dataConverter.stringFromArrayList(episodesWatched)
        watchlistAnimeDao.updateEpisodesWatched(id, episodesString)
    }

    suspend fun updateEpisodesOut(id: Int, episodesOut: Int) = watchlistAnimeDao.updateEpisodesOut(id, episodesOut)

    @Transaction
    suspend fun delete(id: Int) {
        val anime = Anime().apply { mal_id = id }
        val page = withContext(Dispatchers.Unconfined) {
            JikanCacheHandler.getRecommendationPage(anime)
        }
        if (page.request_hash == "") {
            return
        }

        watchlistAnimeDao.delete(id)
        page.recommends.forEach {
            recommendationDao.addCount(it.mal_id, -it.recommendation_count)
            if (recommendationDao.getCountFor(it.mal_id) <= 0) {
                recommendationDao.delete(it.mal_id)
            }
        }
    }

    suspend fun deleteAll() {
        watchlistAnimeDao.deleteAll()
        recommendationDao.deleteAll()
    }

    suspend fun deleteAllRecs() {
        recommendationDao.deleteAll()
    }
}