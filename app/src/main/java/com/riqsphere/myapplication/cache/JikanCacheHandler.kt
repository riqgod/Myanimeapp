package com.riqsphere.myapplication.cache

import com.github.doomsdayrs.jikan4java.types.main.anime.Anime
import com.github.doomsdayrs.jikan4java.types.main.schedule.Schedule
import com.riqsphere.myapplication.utils.Singletons
import com.riqsphere.myapplication.utils.getEpisodesOut
import java.util.*

object JikanCacheHandler {
    private const val ANIME_REQ = 0
    fun getAnime(id: Int): Anime {
        var hash = id.toLong()
        hash = hash * 100 + ANIME_REQ
        val fetch = {
            Singletons.connector.retrieveAnime(id).get()
        }
        return Cache.get(fetch, hash)
    }

    private const val EPISODES_OUT_REQ = 1
    fun getEpisodesOutCount(anime: Anime): Int {
        var hash = anime.mal_id.toLong()
        hash = hash * 100 + EPISODES_OUT_REQ
        val fetch = {
            anime.getEpisodesOut().get()
        }
        return Cache.get(fetch, hash)
    }

    private const val SCHEDULE_REQ = 2
    fun getCurrentSchedule(): Schedule {
        var hash = Calendar.getInstance().get(Calendar.DATE).toLong()
        hash = hash * 100 + SCHEDULE_REQ
        val fetch = {
            Singletons.connector.currentSchedule.get()
        }
        return Cache.get(fetch, hash)
    }
}