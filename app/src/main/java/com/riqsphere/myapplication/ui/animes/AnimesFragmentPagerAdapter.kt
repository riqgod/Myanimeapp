package com.riqsphere.myapplication.ui.animes

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.ui.animes.upcoming.UpcomingFragment
import com.riqsphere.myapplication.ui.animes.watchlist.WatchlistFragment

class AnimesFragmentPagerAdapter(private val context: Context, fm: FragmentManager): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> WatchlistFragment()
            1 -> UpcomingFragment()

            else -> UpcomingFragment() //this doesn't happen!!!!
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 2
    }

    companion object {
        private val TAB_TITLES = arrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}