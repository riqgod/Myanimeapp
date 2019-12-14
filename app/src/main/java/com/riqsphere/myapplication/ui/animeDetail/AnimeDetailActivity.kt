package com.riqsphere.myapplication.ui.animeDetail

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.ui.animeDetail.AnimeDetailFragmentPagerAdapter

class AnimeDetailActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.anime_detail_activity)

        val fragmentPagerAdapter = AnimeDetailFragmentPagerAdapter(this,supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.anime_view_pager)
        viewPager.adapter = fragmentPagerAdapter
        val tabs: TabLayout = this.findViewById(R.id.tabs_anime)
        tabs.setupWithViewPager(viewPager)



    }
    
}