package com.riqsphere.myapplication

import android.graphics.drawable.Drawable
import android.media.Image
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

    }


}