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
    var episodesWatched: IntArray,
    var episodesOut: Int,
    val broadcast: String?
){
    constructor() : this(0, "", "", "", "", 0, IntArray(0), 0, "")

    constructor(anime: Anime) : this(
        id = anime.mal_id,
        title = anime.title,
        title_english = anime.title_english,
        title_japanese = anime.title_japanese,
        imgURL = anime.imageURL,
        episodes = anime.episodes,
        episodesWatched = IntArray(0),
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WatchlistAnime

        if (id != other.id) return false
        if (title != other.title) return false
        if (title_english != other.title_english) return false
        if (title_japanese != other.title_japanese) return false
        if (imgURL != other.imgURL) return false
        if (episodes != other.episodes) return false
        if (!episodesWatched.contentEquals(other.episodesWatched)) return false
        if (episodesOut != other.episodesOut) return false
        if (broadcast != other.broadcast) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + title.hashCode()
        result = 31 * result + title_english.hashCode()
        result = 31 * result + title_japanese.hashCode()
        result = 31 * result + imgURL.hashCode()
        result = 31 * result + episodes
        result = 31 * result + episodesWatched.contentHashCode()
        result = 31 * result + episodesOut
        result = 31 * result + broadcast.hashCode()
        return result
    }
}