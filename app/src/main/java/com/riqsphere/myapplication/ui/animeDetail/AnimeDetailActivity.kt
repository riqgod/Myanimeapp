package com.riqsphere.myapplication.ui.animeDetail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
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

        //get temporary anime to pass to another fragments...
        val mal_id = intent.getStringExtra("mal_id")
        temporaryAnime = temporaryFetch(mal_id.toInt())

        val fragmentPagerAdapter = AnimeDetailFragmentPagerAdapter(this,supportFragmentManager,temporaryAnime)
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
        imageBg.setImageResource(R.drawable.dororo) //temporary
        animeTitle.setText(temporaryAnime.title)
        animeSubtitle.setText(getGenres(temporaryAnime))
        animeScore.setText(temporaryAnime.score.toString())



    }

    fun temporaryFetch(id:Int): Anime {
        return JikanCacheHandler.getAnime(id)
    }

    public fun animeClick(view: View){
        val i = Intent(this, AnimeDetailActivity::class.java)
        val mal_id = view.contentDescription
        i.putExtra("mal_id",mal_id)
        startActivity(i)
    }

    public fun addToWatchListClick(view:View){
        //just a test
        val image:ImageView = view as ImageView
        image.setImageResource(R.drawable.ic_added_to_list)
    }

    public fun epWatchedClick(view:View){
        //just a test
        val image:ImageView = view as ImageView
        image.setImageResource(R.drawable.ic_button_checked_as_watched)
    }

    fun getGenres(anime:Anime): String{
        val genres = anime.genres
        var genresText:String = ""
        var count:Int = 0
        for (genre in genres){
            if(count+1 >= genres.size){
                genresText += genre.name+"."
            }else{
                genresText += genre.name+", "
            }
            count++
        }

        return genresText
    }


}