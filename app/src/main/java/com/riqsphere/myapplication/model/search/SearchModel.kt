package com.riqsphere.myapplication.model.search

import com.github.doomsdayrs.jikan4java.types.main.anime.Anime
import com.riqsphere.myapplication.model.watchlist.room.WatchlistAnime

class SearchModel(
    val imageURL:String,
    val animeTitle: String,
    val score:String,
    val added:Boolean
){
    constructor(anime:Anime, boolean: Boolean):this(
        imageURL = anime.imageURL,
        animeTitle = anime.title,
        score = anime.score.toString(),
        added = boolean // placeholder... should check the watchlist
    )
}