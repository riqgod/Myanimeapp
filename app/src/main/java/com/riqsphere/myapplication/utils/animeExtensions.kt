package com.riqsphere.myapplication.utils

import com.github.doomsdayrs.jikan4java.types.main.anime.Anime
import com.riqsphere.myapplication.model.watchlist.WatchlistAnime
import java.util.concurrent.CompletableFuture

private const val EPS_PER_PAGE = 100

fun WatchlistAnime.getEpisodesOut(): CompletableFuture<Int> {
    val an = Anime()
    an.mal_id = id
    return an.getEpisodesOut()
}

fun Anime.getEpisodesOut(): CompletableFuture<Int> {
    return CompletableFuture.supplyAsync {
        var eps = getEpisodes().get()
        val lastPage = eps.episodes_last_page
        return@supplyAsync if (lastPage != 1) {
            eps = getEpisodes(lastPage).get()
            val previousPagesCount = (lastPage - 1) * EPS_PER_PAGE
            eps.episodes.size + previousPagesCount
        } else {
            eps.episodes.size
        }
    }
}