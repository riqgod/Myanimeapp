package com.riqsphere.myapplication.upcoming

import com.github.doomsdayrs.jikan4java.types.main.anime.Anime
import com.github.doomsdayrs.jikan4java.types.main.schedule.SubAnime
import com.riqsphere.myapplication.watchlist.room.WatchlistAnime
import java.util.concurrent.CompletableFuture

class UpcomingModel(
    val imageURL: String,
    val animeTitle: String,
    val epNum: String,
    val time: String,
    val futureAnime: CompletableFuture<Anime>
) {
    constructor(subAnime: SubAnime, watchlistAnime: WatchlistAnime) : this(
        imageURL = subAnime.image_url,
        animeTitle = subAnime.title,
        epNum = (watchlistAnime.episodesOut + 1).toString(),
        time = watchlistAnime.broadcast,
        futureAnime = subAnime.anime
    )
}