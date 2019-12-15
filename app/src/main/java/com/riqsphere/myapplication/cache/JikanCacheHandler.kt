package com.riqsphere.myapplication.cache

import com.github.doomsdayrs.jikan4java.core.search.TopSearch
import com.github.doomsdayrs.jikan4java.core.search.animemanga.AnimeSearch
import com.github.doomsdayrs.jikan4java.enums.search.animemanga.orderby.AnimeOrderBy
import com.github.doomsdayrs.jikan4java.enums.top.AnimeTops
import com.github.doomsdayrs.jikan4java.enums.top.Tops
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime
import com.github.doomsdayrs.jikan4java.types.main.anime.animePage.AnimePage
import com.github.doomsdayrs.jikan4java.types.main.anime.animePage.AnimePageAnime
import com.github.doomsdayrs.jikan4java.types.main.anime.episodes.Episode
import com.github.doomsdayrs.jikan4java.types.main.anime.episodes.Episodes
import com.github.doomsdayrs.jikan4java.types.main.anime.videos.Video
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
import com.github.doomsdayrs.jikan4java.types.main.anime.videos.Episode as VideoEpisode

object JikanCacheHandler {
    const val INTERNET_UNAVAILABLE = "Internet unavailable!"

    private const val ANIME_REQ = 0
    private val defaultAnime by lazy {
        Anime().apply {
            title = INTERNET_UNAVAILABLE
            imageURL = ""
            title_english = INTERNET_UNAVAILABLE
            title_japanese = INTERNET_UNAVAILABLE
            synopsis = INTERNET_UNAVAILABLE
            broadcast = INTERNET_UNAVAILABLE
        }
    }

    fun getAnime(id: Int): Anime {
        val fetch = {
            try {
                Singletons.connector.retrieveAnime(id).get() ?: defaultAnime
            } catch (e: Error) {
                e.printStackTrace()
                defaultAnime
            }
        }

        var hash = id.toLong()
        hash = hash * 100 + ANIME_REQ

        return Cache.get(fetch, hash)
    }

    private const val EPISODES_OUT_REQ = 1
    private const val defaultEpisodesOut = 100
    fun getEpisodesOutCount(anime: Anime): Int {
        val fetch = {
            try {
                anime.getEpisodesOut().get() ?: defaultEpisodesOut
            } catch (e: Error) {
                e.printStackTrace()
                defaultEpisodesOut
            }
        }

        var hash = anime.mal_id.toLong()
        hash = hash * 100 + EPISODES_OUT_REQ

        return Cache.get(fetch, hash)
    }

    private const val SCHEDULE_REQ = 2
    private val defaultSubAnime by lazy {
        SubAnime().apply {
        title = INTERNET_UNAVAILABLE
        image_url = ""
    }
    }
    private val defaultSchedule by lazy { Schedule().apply {
        sunday = arrayListOf(defaultSubAnime)
        monday = arrayListOf()
        tuesday = monday
        wednesday = monday
        thursday = monday
        friday = monday
        saturday = monday
    } }
    fun getCurrentSchedule(): Schedule {
        val fetch = {
            try {
                Singletons.connector.currentSchedule.get() ?: defaultSchedule
            } catch (e: Error) {
                e.printStackTrace()
                defaultSchedule
            }
        }

        val calendar = Calendar.getInstance()
        val date = calendar.get(Calendar.DATE)
        var hash = date.toLong()
        hash = hash * 100 + SCHEDULE_REQ

        return Cache.get(fetch, hash)
    }

    private val defaultSSA by lazy {
        SeasonSearchAnime().apply {
        title = INTERNET_UNAVAILABLE
        image_url = ""
    }
    }
    private val defaultSeasonArr by lazy { arrayListOf(defaultSSA) }
    private val defaultSeason by lazy {
        SeasonSearch().apply {
        animes = defaultSeasonArr
    }
    }
    private const val CURRENT_SEASON_REQ = 3
    fun getCurrentSeason(): SeasonSearch {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val fetch = {
            try {
                Singletons.connector.seasonSearch(currentYear, calendar.jikanSeason()).get() ?: defaultSeason
            } catch (e: Error) {
                e.printStackTrace()
                defaultSeason
            }
        }

        val month = calendar.get(Calendar.MONTH)
        var hash = month.toLong()
        hash = hash * 100 + CURRENT_SEASON_REQ

        return Cache.get(fetch, hash)
    }

    private val defaultUpcoming by lazy { Top("", false, 0) }
    private const val UPCOMING_REQ = 4
    fun getTopUpcoming(): Top {
        val fetch = {
            try {
                TopSearch().searchTop(Tops.ANIME, AnimeTops.UPCOMING).get() ?: defaultUpcoming
            } catch (e: Error) {
                e.printStackTrace()
                defaultUpcoming
            }
        }

        val calendar = Calendar.getInstance()
        val week = calendar.get(Calendar.WEEK_OF_YEAR)
        var hash = week.toLong()
        hash = hash * 100 + UPCOMING_REQ

        return Cache.get(fetch, hash)
    }

    private val defaultAPA by lazy { AnimePageAnime(0, "", "", INTERNET_UNAVAILABLE, "", "", 0.0, "", "", 0, false, 0, "", "") }
    private val defaultAPAArray by lazy { arrayListOf(defaultAPA) }
    private val defaultAnimePage by lazy {
        AnimePage().apply {
        animes = defaultAPAArray
    }
    }

    private fun getAnimePage(req: Int, hashExtra: Long, specifier: (AnimeSearch) -> AnimeSearch): AnimePage {
        val fetch = {
            try {
                specifier(AnimeSearch()).get().get() ?: defaultAnimePage
            } catch (e: Error) {
                e.printStackTrace()
                defaultAnimePage
            }
        }

        val calendar = Calendar.getInstance()
        val date = calendar.get(Calendar.DATE)
        var hash = date.toLong()
        hash = hash * 31 + hashExtra
        hash = hash * 100 + req

        return Cache.get(fetch, hash)
    }

    private const val MOST_POPLAR_REQ = 5
    fun getMostPoplar() = getAnimePage(MOST_POPLAR_REQ, 0) { it.orderBy(AnimeOrderBy.MEMBERS) }

    private const val TOP_SCORE_REQ = 6
    fun getTopScore() = getAnimePage(TOP_SCORE_REQ, 0) { it.orderBy(AnimeOrderBy.SCORE) }

    private const val SEARCH_REQ = 7
    fun search(query: String) = getAnimePage(SEARCH_REQ, query.hashCode().toLong()) { it.setQuery(query) }

    private val defaultRecommend by lazy {
        Recommend().apply {
        title = INTERNET_UNAVAILABLE
        image_url = ""
    }
    }
    private val defaultRecArr by lazy { arrayListOf(defaultRecommend) }
    private val defaultRecPage by lazy {
        RecommendationPage("", false, 0).apply {
        recommends = defaultRecArr
    }
    }
    private const val REC_PAGE_REQ = 7
    fun getRecommendationPage(anime: Anime): RecommendationPage{
        val fetch = {
            try {
                anime.recommendationPage.get() ?: defaultRecPage
            } catch (e: Error) {
                e.printStackTrace()
                defaultRecPage
            }
        }

        val calendar = Calendar.getInstance()
        val date = calendar.get(Calendar.DATE)
        var hash = date.toLong()
        hash = hash * 31 + anime.mal_id
        hash = hash * 100 + REC_PAGE_REQ

        return Cache.get(fetch, hash)
    }

    private val defaultEpisode by lazy { Episode().apply { this.title = INTERNET_UNAVAILABLE } }
    private val defaultEpisodes by lazy { Episodes().apply { this.episodes = arrayListOf(defaultEpisode) } }
    private const val EPISODES_REQ = 8
    fun getEpisodes(anime: Anime): Episodes {
        val fetch = {
            try {
                anime.getEpisodes().get() ?: defaultEpisodes
            } catch (e: Error) {
                e.printStackTrace()
                defaultEpisodes
            }
        }

        val calendar = Calendar.getInstance()
        val date = calendar.get(Calendar.DATE)
        var hash = date.toLong()
        hash = hash * 31 + anime.mal_id
        hash = hash * 100 + EPISODES_REQ

        return Cache.get(fetch, hash)
    }

    private val defaultVideoEpisode by lazy { VideoEpisode().apply {
        title = INTERNET_UNAVAILABLE
        image_url = INTERNET_UNAVAILABLE
    } }
    private val defaultVideo by lazy { Video().apply { episodes = arrayListOf(defaultVideoEpisode) } }
    private const val VIDEO_REQ = 9
    fun getVideo(anime: Anime): Video {
        val fetch = {
            try {
                anime.videos.get() ?: defaultVideo
            } catch (e: Error) {
                e.printStackTrace()
                defaultVideo
            }
        }

        val calendar = Calendar.getInstance()
        val date = calendar.get(Calendar.DATE)
        var hash = date.toLong()
        hash = hash * 31 + anime.mal_id
        hash = hash * 100 + VIDEO_REQ

        return Cache.get(fetch, hash)
    }
}