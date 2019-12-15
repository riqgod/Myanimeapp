package com.riqsphere.myapplication.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Transaction
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime
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
    }

    fun insert(anime: Anime) = insert(
        WatchlistAnime(anime)
    )

    fun delete(id: Int) = viewModelScope.launch { repo.delete(id) }

    @Transaction
    fun addEpisodeWatched(watchlistAnime: WatchlistAnime, episodeToAdd: Int): Boolean {
        if (watchlistAnime.episodesWatched.any { it == episodeToAdd }) {
            return false
        }

        watchlistAnime.episodesWatched = watchlistAnime.episodesWatched.plus(episodeToAdd)
        viewModelScope.launch {
            repo.updateEpisodesWatched(watchlistAnime.id, watchlistAnime.episodesWatched)
        }
        return true
    }

    @Transaction
    fun removeEpisodeWatched(watchlistAnime: WatchlistAnime, episodeToRemove: Int): Boolean {
        if (watchlistAnime.episodesWatched.any { it == episodeToRemove }) {
            return false
        }

        val newWatched = IntArray(watchlistAnime.episodesWatched.size)
        var index = 0
        watchlistAnime.episodesWatched.forEach {
            if (it != episodeToRemove) {
                newWatched[index] = it
                ++index
            }
        }
        viewModelScope.launch {
            repo.updateEpisodesWatched(watchlistAnime.id, watchlistAnime.episodesWatched)
        }
        return true
    }

    fun updateEpisodesOut(id: Int, episodesOut: Int) = viewModelScope.launch {
        repo.updateEpisodesOut(id, episodesOut)
    }

    fun deleteAllRecs() = viewModelScope.launch {
        repo.deleteAllRecs()
    }

    fun deleteAll() = viewModelScope.launch {
        repo.deleteAll()
    }

    companion object {
        private val lock = Object()
    }
}