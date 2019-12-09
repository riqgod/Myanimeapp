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

    fun insert(futureAnime: CompletableFuture<Anime>) = viewModelScope.launch {
        val anime = futureAnime.get()
        insert(anime)
    }

    fun insert(mal_id: Int) = insert(Singletons.connector.retrieveAnime(mal_id))

    @Transaction
    fun addEpisodeWatched(id: Int, episodeWatched: Int) = viewModelScope.launch {
        val episodesWatched = repo.getEpisodesWatched(id)
        episodesWatched.add(episodeWatched)
        repo.updateEpisodesWatched(id, episodesWatched)
    }

    @Transaction
    fun removeEpisodeWatched(id: Int, episodeToRemove: Int) = viewModelScope.launch {
        val episodesWatched = repo.getEpisodesWatched(id)
        episodesWatched.remove(episodeToRemove)
        repo.updateEpisodesWatched(id, episodesWatched)
    }

    @Transaction
    fun addEpisodeWatched(watchlistAnime: WatchlistAnime, episodeWatched: Int) = viewModelScope.launch {
        watchlistAnime.episodesWatched.add(episodeWatched)
        repo.updateEpisodesWatched(watchlistAnime.id, watchlistAnime.episodesWatched)
    }

    @Transaction
    fun removeEpisodeWatched(watchlistAnime: WatchlistAnime, episodeToRemove: Int) = viewModelScope.launch {
        watchlistAnime.episodesWatched.add(episodeToRemove)
        repo.updateEpisodesWatched(watchlistAnime.id, watchlistAnime.episodesWatched)
    }

    fun updateEpisodesOut(id: Int, episodesOut: Int) = viewModelScope.launch {
        repo.updateEpisodesOut(id, episodesOut)
    }

    fun updateEpisodesOut(anime: Anime, episodesOut: Int) = updateEpisodesOut(anime.mal_id, episodesOut)

    fun updateEpisodesOut(anime: Anime) = viewModelScope.launch {
        val episodesOut = anime.getEpisodesOut().get()
        repo.updateEpisodesOut(anime.mal_id,  episodesOut)
    }

    fun deleteAll() = viewModelScope.launch {
        repo.deleteAll()
    }
}