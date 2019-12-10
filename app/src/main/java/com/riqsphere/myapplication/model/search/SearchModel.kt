package com.riqsphere.myapplication.model.search

import com.github.doomsdayrs.jikan4java.types.main.anime.Anime
import com.riqsphere.myapplication.model.watchlist.room.WatchlistAnime

class SearchModel(
    val imageURL:String,
    val animeTitle: String,
    val score:String,
    val added:Boolean
){
    constructor(anime:Anime):this(
        imageURL = anime.imageURL,
        animeTitle = anime.title,
        score = anime.score.toString(),
        added = false // placeholder... should check the watchlist
    )
}