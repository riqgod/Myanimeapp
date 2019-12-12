package com.riqsphere.myapplication.model.search

import com.github.doomsdayrs.jikan4java.types.main.anime.Anime
import com.github.doomsdayrs.jikan4java.types.main.anime.animePage.AnimePageAnime
import com.github.doomsdayrs.jikan4java.types.main.season.SeasonSearchAnime
import com.github.doomsdayrs.jikan4java.types.main.top.TopListing

class SearchModel(
    val id: Int,
    val imageURL: String,
    val animeTitle: String,
    val score: String
){
    constructor(anime: Anime): this(
        id = anime.mal_id,
        imageURL = anime.imageURL,
        animeTitle = anime.title,
        score = anime.score.toString()
    )

    constructor(ssa: SeasonSearchAnime): this (
        id = ssa.mal_id,
        imageURL = ssa.image_url,
        animeTitle = ssa.title,
        score = ssa.score.toString()
    )

    constructor(tl: TopListing): this (
        id = tl.mal_id,
        imageURL = tl.image_url,
        animeTitle = tl.title,
        score = ""
    )

    constructor(apa: AnimePageAnime): this (
        id = apa.mal_id,
        imageURL = apa.image_url,
        animeTitle = apa.title,
        score = apa.score.toString()
    )

    companion object {
        fun arrayListFromSeasonSearch(other: ArrayList<SeasonSearchAnime>): ArrayList<SearchModel> {
            return other.mapTo(arrayListOf(), {
                SearchModel(it)
            })
        }

        fun arrayListFromTopListing(other: ArrayList<TopListing>): ArrayList<SearchModel> {
            return other.mapTo(arrayListOf(), {
                SearchModel(it)
            })
        }

        fun arrayListFromAnimePageAnime(other: ArrayList<AnimePageAnime>): ArrayList<SearchModel> {
            return other.mapTo(arrayListOf(), {
                SearchModel(it)
            })
        }
    }
}