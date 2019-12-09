package com.riqsphere.myapplication.model.watchlist.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Transaction
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime
import com.riqsphere.myapplication.utils.Singletons
import com.riqsphere.myapplication.utils.getEpisodesOut
import kotlinx.coroutines.launch
import java.util.concurrent.CompletableFuture

class WatchlistViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: WatchlistAnimeRepository
    val allWatchlistAnime: LiveData<List<WatchlistAnime>>

    init {
        val watchlistAnimeDao = WatchlistAnimeDatabase.getDatabase(application).watchlistAnimeDao()
        repo = WatchlistAnimeRepository(watchlistAnimeDao)
        allWatchlistAnime = repo.allWatchlistAnime
    }

    fun insert(watchlistAnime: WatchlistAnime) = viewModelScope.launch {
        repo.insert(watchlistAnime)
    }

    fun insert(anime: Anime) = insert(WatchlistAnime(anime))

    fun insert(mal_id: Int) = insert(Singletons.connector.retrieveAnime(mal_id).get())

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

    fun updateEpisodesOut(anime: Anime, episodesOut: Int) = updateEpisodesOut(anime.mal_id, episodesOut)

    fun updateEpisodesOut(anime: Anime) {
        val episodesOut = anime.getEpisodesOut().get()
        viewModelScope.launch {
            repo.updateEpisodesOut(anime.mal_id,  episodesOut)
        }
    }

    fun deleteAll() = viewModelScope.launch {
        repo.deleteAll()
    }

    companion object {
        private val lock = Object()
    }
}