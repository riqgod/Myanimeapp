package com.riqsphere.myapplication.ui.animeDetail

import android.app.Activity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime
import com.google.android.material.tabs.TabLayout
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.cache.JikanCacheHandler
import com.riqsphere.myapplication.ui.animeDetail.AnimeDetailFragmentPagerAdapter

class AnimeDetailActivity : AppCompatActivity(){

    public lateinit var temporaryAnime:Anime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.anime_detail_activity)

        val fragmentPagerAdapter = AnimeDetailFragmentPagerAdapter(this,supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.anime_view_pager)
        viewPager.adapter = fragmentPagerAdapter
        val tabs: TabLayout = this.findViewById(R.id.tabs_anime)
        tabs.setupWithViewPager(viewPager)

        //setting the content
        val imageBg = findViewById<ImageView>(R.id.anime_bg_collapsing_image)
        val animeTitle = findViewById<TextView>(R.id.anime_bg_tittle)
        val animeSubtitle = findViewById<TextView>(R.id.anime_bg_subtitle)
        val animeScore = findViewById<TextView>(R.id.anime_bg_score)

        //temporary feed
        temporaryAnime = temporaryFetch(37520)

        imageBg.setImageResource(R.drawable.dororo) //temporary
        animeTitle.setText(temporaryAnime.title)
        animeSubtitle.setText(temporaryAnime.genres.toString())
        animeScore.setText(temporaryAnime.score.toString())



    }

    fun temporaryFetch(id:Int): Anime {
        return JikanCacheHandler.getAnime(id)
    }


}