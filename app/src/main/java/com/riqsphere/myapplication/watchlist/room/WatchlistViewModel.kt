package com.riqsphere.myapplication.watchlist.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime
import kotlinx.coroutines.launch

class WatchlistViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: WatchlistAnimeRepository
    private val allWatchlistAnime: LiveData<List<WatchlistAnime>>

    init {
        val watchlistAnimeDao = WatchlistAnimeDatabase.getDatabase(application).watchlistAnimeDao()
        repo = WatchlistAnimeRepository(watchlistAnimeDao)
        allWatchlistAnime = repo.allWatchlistAnime
    }

    fun insert(watchlistAnime: WatchlistAnime) = viewModelScope.launch {
        repo.insert(watchlistAnime)
    }

    fun addEpisodeWatched(episode: Int, id: Int) = viewModelScope.launch {
        repo.addEpisodeWatched(episode, id)
    }

    fun addEpisodeWatched(episode: Int, anime: WatchlistAnime) = addEpisodeWatched(episode, anime.id)

    fun addEpisodeWatched(episode: Int, anime: Anime) = addEpisodeWatched(episode, anime.mal_id)

    fun deleteAll() = viewModelScope.launch {
        repo.deleteAll()
    }
}