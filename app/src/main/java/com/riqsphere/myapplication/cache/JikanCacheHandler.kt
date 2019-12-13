package com.riqsphere.myapplication.cache

import com.github.doomsdayrs.jikan4java.core.search.TopSearch
import com.github.doomsdayrs.jikan4java.core.search.animemanga.AnimeSearch
import com.github.doomsdayrs.jikan4java.enums.search.animemanga.orderby.AnimeOrderBy
import com.github.doomsdayrs.jikan4java.enums.top.AnimeTops
import com.github.doomsdayrs.jikan4java.enums.top.Tops
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime
import com.github.doomsdayrs.jikan4java.types.main.anime.animePage.AnimePageAnime
import com.github.doomsdayrs.jikan4java.types.main.schedule.Schedule
import com.github.doomsdayrs.jikan4java.types.main.season.SeasonSearchAnime
import com.github.doomsdayrs.jikan4java.types.main.top.TopListing
import com.github.doomsdayrs.jikan4java.types.support.recommendations.Recommend
import com.riqsphere.myapplication.utils.Singletons
import com.riqsphere.myapplication.utils.getEpisodesOut
import com.riqsphere.myapplication.utils.jikanSeason
import java.util.*

object JikanCacheHandler {
    private const val ANIME_REQ = 0
    fun getAnime(id: Int): Anime {
        val fetch = {
            Singletons.connector.retrieveAnime(id).get()
        }

        var hash = id.toLong()
        hash = hash * 100 + ANIME_REQ

        return Cache.get(fetch, hash)
    }

    private const val EPISODES_OUT_REQ = 1
    fun getEpisodesOutCount(anime: Anime): Int {
        val fetch = {
            anime.getEpisodesOut().get()
        }

        var hash = anime.mal_id.toLong()
        hash = hash * 100 + EPISODES_OUT_REQ

        return Cache.get(fetch, hash)
    }

    private const val SCHEDULE_REQ = 2
    fun getCurrentSchedule(): Schedule {
        val fetch = {
            Singletons.connector.currentSchedule.get()
        }

        val calendar = Calendar.getInstance()
        val date = calendar.get(Calendar.DATE)
        var hash = date.toLong()
        hash = hash * 100 + SCHEDULE_REQ

        return Cache.get(fetch, hash)
    }

    private const val CURRENT_SEASON_REQ = 3
    fun getCurrentSeason(): ArrayList<SeasonSearchAnime> {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val fetch = {
            Singletons.connector.seasonSearch(currentYear, calendar.jikanSeason()).get().animes
        }

        val month = calendar.get(Calendar.MONTH)
        var hash = month.toLong()
        hash = hash * 100 + CURRENT_SEASON_REQ

        return Cache.get(fetch, hash)
    }

    private const val UPCOMING_REQ = 4
    fun getTopUpcoming(): ArrayList<TopListing> {
        val fetch = {
            TopSearch().searchTop(Tops.ANIME, AnimeTops.UPCOMING).get().topListings
        }

        val calendar = Calendar.getInstance()
        val week = calendar.get(Calendar.WEEK_OF_YEAR)
        var hash = week.toLong()
        hash = hash * 100 + UPCOMING_REQ

        return Cache.get(fetch, hash)
    }

    private fun getPoplarScoreCommon(req: Int, orderBy: AnimeOrderBy): ArrayList<AnimePageAnime> {
        val fetch = {
            AnimeSearch().orderBy(orderBy).get().get().animes
        }

        val calendar = Calendar.getInstance()
        val date = calendar.get(Calendar.DATE)
        var hash = date.toLong()
        hash = hash * 100 + req

        return Cache.get(fetch, hash)
    }

    private const val MOST_POPLAR_REQ = 5
    fun getMostPoplar() = getPoplarScoreCommon(MOST_POPLAR_REQ, AnimeOrderBy.MEMBERS)

    private const val TOP_SCORE_REQ = 6
    fun getTopScore() = getPoplarScoreCommon(TOP_SCORE_REQ, AnimeOrderBy.SCORE)

    private const val REC_PAGE_REQ = 7
    fun getRecommendationPage(anime: Anime): ArrayList<Recommend>{
        val fetch = {
            anime.recommendationPage.get().recommends
        }

        val calendar = Calendar.getInstance()
        val date = calendar.get(Calendar.DATE)
        var hash = date.toLong()
        hash = hash * 100 + REC_PAGE_REQ

        return Cache.get(fetch, hash)
    }
}