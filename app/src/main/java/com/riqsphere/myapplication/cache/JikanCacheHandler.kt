package com.riqsphere.myapplication.cache

import com.github.doomsdayrs.jikan4java.core.search.TopSearch
import com.github.doomsdayrs.jikan4java.core.search.animemanga.AnimeSearch
import com.github.doomsdayrs.jikan4java.enums.search.animemanga.orderby.AnimeOrderBy
import com.github.doomsdayrs.jikan4java.enums.top.AnimeTops
import com.github.doomsdayrs.jikan4java.enums.top.Tops
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime
import com.github.doomsdayrs.jikan4java.types.main.anime.animePage.AnimePage
import com.github.doomsdayrs.jikan4java.types.main.anime.animePage.AnimePageAnime
import com.github.doomsdayrs.jikan4java.types.main.schedule.Schedule
import com.github.doomsdayrs.jikan4java.types.main.schedule.SubAnime
import com.github.doomsdayrs.jikan4java.types.main.season.SeasonSearch
import com.github.doomsdayrs.jikan4java.types.main.season.SeasonSearchAnime
import com.github.doomsdayrs.jikan4java.types.main.top.Top
import com.github.doomsdayrs.jikan4java.types.support.recommendations.Recommend
import com.github.doomsdayrs.jikan4java.types.support.recommendations.RecommendationPage
import com.riqsphere.myapplication.utils.Singletons
import com.riqsphere.myapplication.utils.getEpisodesOut
import com.riqsphere.myapplication.utils.jikanSeason
import java.util.*

object JikanCacheHandler {
    private const val INTERNET_UNAVAILABLE = "Internet unavailable!"

    private const val ANIME_REQ = 0
    private val defaultAnime = Anime().apply {
        title = INTERNET_UNAVAILABLE
        imageURL = ""
        title_english = INTERNET_UNAVAILABLE
        title_japanese = INTERNET_UNAVAILABLE
    }
    fun getAnime(id: Int): Anime {
        val fetch = {
            Singletons.connector.retrieveAnime(id).get()
        }

        var hash = id.toLong()
        hash = hash * 100 + ANIME_REQ

        return Cache.get(fetch, defaultAnime, hash)
    }

    private const val EPISODES_OUT_REQ = 1
    private const val defaultEpisodes = 100
    fun getEpisodesOutCount(anime: Anime): Int {
        val fetch = {
            anime.getEpisodesOut().get() ?: defaultEpisodes
        }

        var hash = anime.mal_id.toLong()
        hash = hash * 100 + EPISODES_OUT_REQ

        return Cache.get(fetch, defaultEpisodes, hash)
    }

    private const val SCHEDULE_REQ = 2
    private val defaultSubAnime = SubAnime().apply {
        title = INTERNET_UNAVAILABLE
        image_url = ""
    }
    private val defaultSchedule = Schedule().apply {
        sunday = arrayListOf(defaultSubAnime)
        monday = arrayListOf()
        tuesday = monday
        wednesday = monday
        thursday = monday
        friday = monday
        saturday = monday

    }
    fun getCurrentSchedule(): Schedule {
        val fetch = {
            Singletons.connector.currentSchedule.get()?: defaultSchedule
        }

        val calendar = Calendar.getInstance()
        val date = calendar.get(Calendar.DATE)
        var hash = date.toLong()
        hash = hash * 100 + SCHEDULE_REQ

        return Cache.get(fetch, defaultSchedule, hash)
    }

    private val defaultSSA = SeasonSearchAnime().apply {
        title = INTERNET_UNAVAILABLE
        image_url = ""
    }
    private val defaultSeasonArr = arrayListOf(defaultSSA)
    private val defaultSeason = SeasonSearch().apply {
        animes = defaultSeasonArr
    }
    private const val CURRENT_SEASON_REQ = 3
    fun getCurrentSeason(): SeasonSearch {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val fetch = {
            Singletons.connector.seasonSearch(currentYear, calendar.jikanSeason()).get()
        }

        val month = calendar.get(Calendar.MONTH)
        var hash = month.toLong()
        hash = hash * 100 + CURRENT_SEASON_REQ

        return Cache.get(fetch, defaultSeason, hash)
    }

    private val defaultUpcoming = Top("", false, 0)
    private const val UPCOMING_REQ = 4
    fun getTopUpcoming(): Top {
        val fetch = {
            TopSearch().searchTop(Tops.ANIME, AnimeTops.UPCOMING).get()
        }

        val calendar = Calendar.getInstance()
        val week = calendar.get(Calendar.WEEK_OF_YEAR)
        var hash = week.toLong()
        hash = hash * 100 + UPCOMING_REQ

        return Cache.get(fetch, defaultUpcoming, hash)
    }

    private val defaultAPA = AnimePageAnime(0, "", "", INTERNET_UNAVAILABLE, "", "", 0.0, "", "", 0, false, 0, "", "")
    private val defaultAPAArray = arrayListOf(defaultAPA)
    private val defaultAnimePage = AnimePage().apply {
        animes = defaultAPAArray
    }
    private fun getAnimePage(req: Int, specifier: (AnimeSearch) -> AnimeSearch ): AnimePage {
        val fetch = {
            specifier(AnimeSearch()).get().get()
        }

        val calendar = Calendar.getInstance()
        val date = calendar.get(Calendar.DATE)
        var hash = date.toLong()
        hash = hash * 100 + req

        return Cache.get(fetch, defaultAnimePage, hash)
    }

    private const val MOST_POPLAR_REQ = 5
    fun getMostPoplar() = getAnimePage(MOST_POPLAR_REQ) { it.orderBy(AnimeOrderBy.MEMBERS) }

    private const val TOP_SCORE_REQ = 6
    fun getTopScore() = getAnimePage(TOP_SCORE_REQ) { it.orderBy(AnimeOrderBy.SCORE) }

    private const val SEARCH_REQ = 7
    fun search(query: String) = getAnimePage(SEARCH_REQ) { it.setQuery(query) }

    private val defaultRecommend = Recommend().apply {
        title = INTERNET_UNAVAILABLE
        image_url = ""
    }
    private val defaultRecArr = arrayListOf(defaultRecommend)
    private val defaultRecPage = RecommendationPage("", false, 0).apply {
        recommends = defaultRecArr
    }
    private const val REC_PAGE_REQ = 7
    fun getRecommendationPage(anime: Anime): RecommendationPage{
        val fetch = {
            anime.recommendationPage.get()
        }

        val calendar = Calendar.getInstance()
        val date = calendar.get(Calendar.DATE)
        var hash = date.toLong()
        hash = hash * 100 + REC_PAGE_REQ

        return Cache.get(fetch, defaultRecPage, hash)
    }
}