package com.riqsphere.myapplication.watchlist.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.github.doomsdayrs.jikan4java.core.Connector
import com.github.doomsdayrs.jikan4java.core.Retriever
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime
import com.github.doomsdayrs.jikan4java.types.main.anime.animePage.AnimePageAnime
import com.google.gson.Gson
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

@Entity(tableName = "watchlist_anime")
data class WatchlistAnime(
    @PrimaryKey
    val id: Int,
    val title: String,
    val title_english: String,
    val title_japanese: String,
    val imgURL: String,
    val episodes: Int,
    var episodesOut: Int,
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
        episodesOut = 0,
        broadcast = anime.broadcast
    ) {
        episodesOut = updatedEpsOut(anime)
    }

    fun updatedEpsOut(anime: Anime): Int {
        return if (anime.mal_id != id) {
            -1
        } else {
            var eps = anime.getEpisodes().get()
            val lastPage = eps.episodes_last_page
            if (lastPage != 1) {
                eps = anime.getEpisodes(lastPage).get()
                val previousPagesCount = (lastPage - 1) * 100
                eps.episodes.size + previousPagesCount
            } else {
                eps.episodes.size
            }
        }
    }

    fun updatedEpsOut() = updatedEpsOut(toAnime().get())

    fun toAnime() = connector.retrieveAnime(id)

    companion object {
        private val connector = Connector()
    }
}