package com.riqsphere.myapplication.ui.animeDetail

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime
import com.github.doomsdayrs.jikan4java.types.main.anime.videos.Video
import com.github.doomsdayrs.jikan4java.types.support.recommendations.RecommendationPage
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.ui.animeDetail.about.AboutFragment
import com.riqsphere.myapplication.ui.animeDetail.episodes.EpisodesFragment
import com.riqsphere.myapplication.ui.animeDetail.recs.RecsFragment

class AnimeDetailFragmentPagerAdapter(
    private val context: Context,
    animus: Anime,
    fm: FragmentManager
): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    private val aboutFragment = AboutFragment()
    private val episodesFragment = EpisodesFragment(animus.mal_id)
    private val recsFragment = RecsFragment()

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> aboutFragment
            1 -> episodesFragment
            2 -> recsFragment

            else -> aboutFragment //this doesn't happen!!!!
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
       return 3
    }

    companion object {
        private val TAB_TITLES = arrayOf(
            R.string.tab_anime_about,
            R.string.tab_anime_episodes,
            R.string.tab_anime_recs
        )
    }

    fun setAnime(anime: Anime) = aboutFragment.setAnime(anime)

    fun setEpisodes(video: Video) = episodesFragment.setEpisodes(video)

    fun setRecs(recs: RecommendationPage) = recsFragment.setRecs(recs)
}