package com.riqsphere.myapplication.model.watchlist.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.github.doomsdayrs.jikan4java.core.Connector
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime
import com.google.gson.Gson
import com.riqsphere.myapplication.utils.Singletons
import com.riqsphere.myapplication.utils.getEpisodesOut
import java.io.Serializable

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
        episodesOut = anime.getEpisodesOut().get(),
        broadcast = anime.broadcast
    )

    fun toAnime() = Singletons.connector.retrieveAnime(id)
}