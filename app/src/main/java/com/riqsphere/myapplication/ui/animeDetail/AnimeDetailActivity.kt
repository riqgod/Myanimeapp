package com.riqsphere.myapplication.ui.animeDetail

import android.os.AsyncTask
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
import com.riqsphere.myapplication.room.MyaaViewModel
import com.riqsphere.myapplication.utils.ImageHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class AnimeDetailActivity : AppCompatActivity() {

    private lateinit var myaaViewModel: MyaaViewModel

    private var id = -1
    private lateinit var fragmentPagerAdapter: AnimeDetailFragmentPagerAdapter

    private lateinit var partialAnime: Anime

    private lateinit var viewPager: ViewPager
    private lateinit var tabs: TabLayout
    private lateinit var imageBg: ImageView
    private lateinit var animeTitle: TextView
    private lateinit var animeSubtitle: TextView
    private lateinit var animeScore: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.anime_detail_activity)

        id = intent.getIntExtra("id", -1)
        if (id == -1) {
            return
        }

        myaaViewModel = MyaaViewModel(application)
        partialAnime = Anime().apply { mal_id = id }

        fragmentPagerAdapter = AnimeDetailFragmentPagerAdapter(
            this,
            partialAnime,
            supportFragmentManager
        )
        viewPager = findViewById(R.id.anime_view_pager)
        viewPager.adapter = fragmentPagerAdapter
        tabs = findViewById(R.id.tabs_anime)
        tabs.setupWithViewPager(viewPager)

        //setting the content
        imageBg = findViewById(R.id.anime_bg_collapsing_image)
        animeTitle = findViewById(R.id.anime_bg_title)
        animeSubtitle = findViewById(R.id.anime_bg_subtitle)
        animeScore = findViewById(R.id.anime_bg_score)

        fetchAnime()
        fetchVideo()
        fetchRecs()
    }

    private fun fetchAnime() {
        AsyncFetcherSetter({
            JikanCacheHandler.getAnime(id)
        }, {
            setActivityData(it)
            fragmentPagerAdapter.setAnime(it)
        }).execute()
    }

    private fun fetchVideo() {
        AsyncFetcherSetter({
            JikanCacheHandler.getVideo(partialAnime)
        }, {
            fragmentPagerAdapter.setEpisodes(it)
        }).execute()
    }

    private fun fetchRecs() {
        AsyncFetcherSetter({
            JikanCacheHandler.getRecommendationPage(partialAnime)
        }, {
            fragmentPagerAdapter.setRecs(it)
        }).execute()
    }

    private fun setActivityData(anime: Anime) {
        ImageHandler.getInstance(this).load(R.drawable.dororo).into(imageBg)
        animeTitle.text = anime.title
        animeSubtitle.text = getGenres(anime)
        animeScore.text = anime.score.toString()
    }

    private fun getGenres(anime: Anime): String{
        val genres = anime.genres ?: return "Loading..."
        var genresText = ""
        for ((count, genre) in genres.withIndex()){
            genresText += if(count+1 >= genres.size){
                genresText += genre.name+"."
            }else{
                genre.name + ", "
            }
        }
        return genresText
    }

    private class AsyncFetcherSetter<T>(private val fetch: () -> T, private val post: (T) -> Unit) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? = runBlocking {
            val result = withContext(Dispatchers.Unconfined){ fetch() }
            withContext(Dispatchers.Main) { post(result) }
            null
        }
    }
}