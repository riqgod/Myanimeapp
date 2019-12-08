package com.riqsphere.myapplication.ui.main

import android.os.Bundle
import android.preference.PreferenceManager
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.model.watchlist.room.WatchlistViewModel

private const val PREFERENCE_FIRST_RUN = "first_run"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val p = PreferenceManager.getDefaultSharedPreferences(this)
        if (p.getBoolean(PREFERENCE_FIRST_RUN, true)) {
            installTasks()
            p.edit().putBoolean(PREFERENCE_FIRST_RUN, false).apply()
        }

        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter =
            SectionsPagerAdapter(
                this,
                supportFragmentManager
            )
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = this.findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
    }

    private fun installTasks() {

    }
}