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
@TypeConverters(EpisodeConverter::class)
data class WatchlistAnime (
    @PrimaryKey
    var id: Int,
    val title: String,
    val title_english: String,
    val title_japanese: String,
    val episodes: Int,
    val episodesWatched: Array<Int>
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WatchlistAnime

        if (id != other.id) return false
        if (title != other.title) return false
        if (title_english != other.title_english) return false
        if (title_japanese != other.title_japanese) return false
        if (episodes != other.episodes) return false
        if (!episodesWatched.contentEquals(other.episodesWatched)) return false

        return true
    }
    override fun hashCode(): Int {
        var result = id
        result = 31 * result + title.hashCode()
        result = 31 * result + title_english.hashCode()
        result = 31 * result + title_japanese.hashCode()
        result = 31 * result + episodes
        result = 31 * result + episodesWatched.contentHashCode()
        return result
    }

    constructor(anime: Anime) : this(anime.mal_id, anime.title, anime.title_english, anime.title_japanese, anime.episodes, arrayOf())

    //TODO: this is a really dirty hack, find a better way to do this
    fun toAnime() : CompletableFuture<Anime> {
        val apa = AnimePageAnime()
        apa.mal_id = id
        return apa.anime
    }
}

object EpisodeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromArray(arr: Array<Int>?): String? {
        var result: String? = null
        arr?.let { result = gson.toJson(it) }
        return result
    }

    @TypeConverter
    fun toArray(str: String?): Array<Int>? {
        var result: Array<Int>? = null
        str?.let { result = gson.fromJson(str, Array<Int>::class.java)}
        return result
    }
}