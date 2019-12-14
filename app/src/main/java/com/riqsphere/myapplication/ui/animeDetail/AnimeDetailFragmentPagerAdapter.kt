package com.riqsphere.myapplication.ui.animeDetail

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.ui.animeDetail.about.AboutFragment
import com.riqsphere.myapplication.ui.animeDetail.episodes.EpisodesFragment
import com.riqsphere.myapplication.ui.animeDetail.recs.RecsFragment

class AnimeDetailFragmentPagerAdapter(private val context: Context, fm: FragmentManager): FragmentPagerAdapter(fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){


    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> AboutFragment()
            1 -> EpisodesFragment()
            2 -> RecsFragment()

            else -> AboutFragment() //this doesn't happen!!!!
        }
    }


    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(AnimeDetailFragmentPagerAdapter.TAB_TITLES[position])
    }

    override fun getCount(): Int {
       return 3;
    }

    companion object {
        private val TAB_TITLES = arrayOf(
            R.string.tab_anime_about,
            R.string.tab_anime_episodes,
            R.string.tab_anime_recs
        )
    }
}