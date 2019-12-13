package com.riqsphere.myapplication.model.watchlist

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime
import com.riqsphere.myapplication.cache.JikanCacheHandler

@Entity(tableName = "watchlist_anime")
data class WatchlistAnime(
    @PrimaryKey
    val id: Int,
    val title: String,
    val title_english: String,
    val title_japanese: String,
    val imgURL: String,
    val episodes: Int,
    val episodesWatched: ArrayList<Int>,
    var episodesOut: Int,
    val broadcast: String
){
    constructor() : this(0, "", "", "", "", 0, arrayListOf(), 0, "")

    constructor(anime: Anime) : this(
        id = anime.mal_id,
        title = anime.title,
        title_english = anime.title_english,
        title_japanese = anime.title_japanese,
        imgURL = anime.imageURL,
        episodes = anime.episodes,
        episodesWatched = arrayListOf(),
        episodesOut = JikanCacheHandler.getEpisodesOutCount(anime),
        broadcast = anime.broadcast
    )

    fun toAnime(): Anime {
        val anime = Anime()
        anime.mal_id = id
        anime.title = title
        anime.title_english = title_english
        anime.title_japanese = title_japanese
        anime.imageURL = imgURL
        anime.episodes = episodes
        anime.broadcast = broadcast

        return anime
    }
}