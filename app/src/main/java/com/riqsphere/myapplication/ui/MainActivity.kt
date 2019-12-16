package com.riqsphere.myapplication.ui

import android.os.Bundle
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.cache.JikanCacheHandler
import com.riqsphere.myapplication.model.watchlist.WatchlistAnime
import com.riqsphere.myapplication.room.MyaaViewModel
import com.riqsphere.myapplication.tasks.WatchlistAlarm
import com.riqsphere.myapplication.ui.animes.AnimesFragment
import com.riqsphere.myapplication.ui.animes.SettingsFragment
import com.riqsphere.myapplication.ui.discover.DiscoverFragment


private const val PREFERENCE_FIRST_RUN = "first_run"


class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
        if (BuildConfig.DEBUG) {
            AndroidDevMetrics.initWith(this);
        }
      */

        val p = getDefaultSharedPreferences(this)
        if (p.getBoolean(PREFERENCE_FIRST_RUN, true)) {
            installTasks()
            p.edit().putBoolean(PREFERENCE_FIRST_RUN, false).apply()
        }

        setContentView(R.layout.activity_main)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener { menuItem ->
            val selectedFragment =
                when (menuItem.itemId) {
                    R.id.nav_animes -> AnimesFragment()
                    R.id.nav_discover -> DiscoverFragment()
                    R.id.nav_settings -> SettingsFragment()
                    else -> null
            }
            selectedFragment?.let { navigateTo(it) }
            true
        }

        navigateToDefaultPage()
    }


    private fun navigateTo(it: Fragment) = supportFragmentManager.beginTransaction().replace(R.id.bottom_navigation_fragment_container, it).commit()

    private fun navigateToDefaultPage() = navigateToAnimes()

    private fun navigateToAnimes() = supportFragmentManager.beginTransaction().replace(R.id.bottom_navigation_fragment_container, AnimesFragment()).commit()

    private fun installTasks() {
        WatchlistAlarm.setAlarm(application)

        val myaaViewModel = MyaaViewModel(application)
        val naruto = WatchlistAnime(
            temporaryFetch(20)
        )
        myaaViewModel.insert(naruto) //naruto
        val hero4 = WatchlistAnime(
            temporaryFetch(38408)
        )
        myaaViewModel.insert(hero4) //boku no hero academia 4
        val nrt = WatchlistAnime(
            temporaryFetch(20)
        )
        myaaViewModel.insert(nrt) //naruto
        val random = WatchlistAnime(
            temporaryFetch(21)
        )
        myaaViewModel.insert(random) //random id
        val rdm = WatchlistAnime(
            temporaryFetch(22)
        )
        myaaViewModel.insert(rdm)
    }

    private fun temporaryFetch(id: Int) = JikanCacheHandler.getAnime(id)
}