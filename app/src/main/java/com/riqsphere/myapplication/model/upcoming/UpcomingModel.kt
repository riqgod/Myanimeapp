package com.riqsphere.myapplication.model.upcoming

import com.riqsphere.myapplication.model.watchlist.room.WatchlistAnime

class UpcomingModel(
    val imageURL: String,
    val animeTitle: String,
    val epNum: String,
    val time: String
) {
    constructor(watchlistAnime: WatchlistAnime) : this(
        imageURL = watchlistAnime.imgURL,
        animeTitle = watchlistAnime.title,
        epNum = (watchlistAnime.episodesOut + 1).toString(),
        time = watchlistAnime.broadcast
    )
}