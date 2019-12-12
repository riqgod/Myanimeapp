package com.riqsphere.myapplication.model.search

import com.github.doomsdayrs.jikan4java.types.main.anime.Anime

class SearchModel(
    val id: Int,
    val imageURL: String,
    val animeTitle: String,
    val score: String
){
    constructor(anime:Anime): this(
        id = anime.mal_id,
        imageURL = anime.imageURL,
        animeTitle = anime.title,
        score = anime.score.toString()
    )
}