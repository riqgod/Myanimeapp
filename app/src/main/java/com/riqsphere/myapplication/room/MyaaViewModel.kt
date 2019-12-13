package com.riqsphere.myapplication.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Transaction
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime
import com.riqsphere.myapplication.cache.JikanCacheHandler
import com.riqsphere.myapplication.model.recommendations.Recommendation
import com.riqsphere.myapplication.model.watchlist.WatchlistAnime
import kotlinx.coroutines.launch

class MyaaViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: MyaaRepository
    val allWatchlistAnime: LiveData<List<WatchlistAnime>>
    val allRecommendation: LiveData<List<Recommendation>>

    init {
        val db =
            MyaaDatabase.getDatabase(
                application
            )
        val watchlistAnimeDao = db.watchlistAnimeDao()
        val recommendationDao = db.recommendationDao()
        repo = MyaaRepository(
            watchlistAnimeDao,
            recommendationDao
        )
        allWatchlistAnime = repo.allWatchlistAnime
        allRecommendation = repo.allRecommendation
    }

    fun insert(watchlistAnime: WatchlistAnime) = viewModelScope.launch {
        repo.insert(watchlistAnime)
        launch {
            val anime = watchlistAnime.toAnime()
            val recommend = JikanCacheHandler.getRecommendationPage(anime)
            val recommendation = Recommendation.arrayListFrom(watchlistAnime.id, recommend)
            recommendation.forEach {
                repo.insert(it)
            }
        }
    }

    fun insert(anime: Anime) = insert(
        WatchlistAnime(
            anime
        )
    )

    @Transaction
    fun addEpisodeWatched(watchlistAnime: WatchlistAnime, episodeToAdd: Int): Boolean {
        val success = watchlistAnime.episodesWatched.add(episodeToAdd)
        if (!success) {
            return false
        }
        viewModelScope.launch {
            repo.updateEpisodesWatched(watchlistAnime.id, watchlistAnime.episodesWatched)
        }
        return true
    }

    @Transaction
    fun removeEpisodeWatched(watchlistAnime: WatchlistAnime, episodeToRemove: Int): Boolean {
        val success = watchlistAnime.episodesWatched.remove(episodeToRemove)
        if (!success) {
            return false
        }
        viewModelScope.launch {
            repo.updateEpisodesWatched(watchlistAnime.id, watchlistAnime.episodesWatched)
        }
        return true
    }

    fun updateEpisodesOut(id: Int, episodesOut: Int) = viewModelScope.launch {
        repo.updateEpisodesOut(id, episodesOut)
    }

    fun deleteAll() = viewModelScope.launch {
        repo.deleteAll()
    }

    companion object {
        private val lock = Object()
    }
}