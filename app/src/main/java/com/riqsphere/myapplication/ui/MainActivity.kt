package com.riqsphere.myapplication.ui

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.model.watchlist.alarm.WatchlistAlarm
import com.riqsphere.myapplication.ui.animes.AnimesFragment
import com.riqsphere.myapplication.ui.animes.DiscoverFragment
import com.riqsphere.myapplication.ui.animes.SettingsFragment
import kotlinx.android.synthetic.main.search_activity.view.*


private const val PREFERENCE_FIRST_RUN = "first_run"

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val p = PreferenceManager.getDefaultSharedPreferences(this)
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

    private fun handleIntent(intent:Intent) {
        if (Intent.ACTION_SEARCH.equals(intent.action)) {
            val query: String = intent.getStringExtra(SearchManager.QUERY)
            doMySearch(query);
        }
    }

    private fun doMySearch(query: String) {
        //not implemented yet
    }

    private fun navigateTo(it: Fragment) = supportFragmentManager.beginTransaction().replace(R.id.bottom_navigation_fragment_container, it).commit()

    private fun navigateToDefaultPage() = navigateToAnimes()

    private fun navigateToAnimes() = supportFragmentManager.beginTransaction().replace(R.id.bottom_navigation_fragment_container, AnimesFragment()).commit()

    private fun installTasks() {
        WatchlistAlarm.setAlarm(this.application)
    }

}