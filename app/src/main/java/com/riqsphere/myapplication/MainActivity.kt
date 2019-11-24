package com.riqsphere.myapplication

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.MainThread
import com.github.doomsdayrs.jikan4java.core.search.animemanga.AnimeSearch
import com.github.doomsdayrs.jikan4java.enums.search.animemanga.orderby.AnimeOrderBy
import com.riqsphere.myapplication.ui.main.SectionsPagerAdapter
import java.lang.Exception
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = this.findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = findViewById(R.id.fab)

        fab.setOnClickListener { view ->
            val anise = AnimeSearch()
            anise.setQuery("lain")
            val futureAni = anise.first
            Snackbar.make(view, "HELLO", Snackbar.LENGTH_LONG).setAction("Action", null).show()
            Thread(Runnable {
                val lain = futureAni.get()
                val themes = lain.opening_themes.joinToString()
                this@MainActivity.runOnUiThread {
                    Snackbar.make(view, themes, Snackbar.LENGTH_LONG).setAction("Action", null).show()
                }
            }).start()
        }
    }
}