package com.riqsphere.myapplication.model.watchlist.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
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

    fun insert(futureAnime: CompletableFuture<Anime>) = viewModelScope.launch {
        val anime = futureAnime.get()
        insert(anime)
    }

    fun insert(mal_id: Int) = insert(Singletons.connector.retrieveAnime(mal_id))

    fun updateEpisodesOut(id: Int, episodes: Int) = viewModelScope.launch {
        repo.updateEpisodesOut(id, episodes)
    }

    fun updateEpisodesOut(anime: Anime, episodes: Int) = updateEpisodesOut(anime.mal_id, episodes)

    fun updateEpisodesOut(anime: Anime) = viewModelScope.launch {
        val episodes = anime.getEpisodesOut().get()
        updateEpisodesOut(anime,  episodes)
    }

    fun deleteAll() = viewModelScope.launch {
        repo.deleteAll()
    }
}