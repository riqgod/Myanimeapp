package com.riqsphere.myapplication.watchlist.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime
import com.github.doomsdayrs.jikan4java.types.main.anime.animePage.AnimePageAnime
import com.google.gson.Gson
import java.util.concurrent.CompletableFuture

@Entity(tableName = "watchlist_anime")
data class WatchlistAnime(
    @PrimaryKey
    val id: Int,
    val title: String,
    val title_english: String,
    val title_japanese: String,
    val imgURL: String,
    val episodes: Int,
    val episodesOut: Int,
    val broadcast: String
){
    constructor() : this(0, "", "", "", "", 0, 0, "")

    constructor(anime: Anime) : this(
        id = anime.mal_id,
        title = anime.title,
        title_english = anime.title_english,
        title_japanese = anime.title_japanese,
        imgURL = anime.imageURL,
        episodes = anime.episodes,
        episodesOut = anime.episode.get().episodes.size,
        broadcast = anime.broadcast
    )

    //TODO: this is a really dirty hack, find a better way to do this
    fun toAnime() : CompletableFuture<Anime> {
        val apa = AnimePageAnime()
        apa.mal_id = id
        return apa.anime
    }
}