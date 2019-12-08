package com.riqsphere.myapplication.model.upcoming

import com.github.doomsdayrs.jikan4java.types.main.anime.Anime
import com.riqsphere.myapplication.model.watchlist.room.WatchlistAnime
import java.util.concurrent.CompletableFuture

class UpcomingModel(
    val imageURL: String,
    val animeTitle: String,
    val epNum: String,
    val time: String,
    val futureAnime: CompletableFuture<Anime>
) {
    constructor(watchlistAnime: WatchlistAnime) : this(
        imageURL = watchlistAnime.imgURL,
        animeTitle = watchlistAnime.title,
        epNum = (watchlistAnime.episodesOut + 1).toString(),
        time = watchlistAnime.broadcast,
        futureAnime = watchlistAnime.toAnime()
    )
}