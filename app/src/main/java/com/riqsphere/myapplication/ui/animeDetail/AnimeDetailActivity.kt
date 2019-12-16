package com.riqsphere.myapplication.ui.animeDetail

import android.os.AsyncTask
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime
import com.google.android.material.tabs.TabLayout
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.cache.JikanCacheHandler
import com.riqsphere.myapplication.utils.ImageHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class AnimeDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.anime_detail_activity)

        val id = intent.getIntExtra("id", -1)
        if (id == -1) {
            return
        }

        val partialAnime = Anime().apply { mal_id = id }

        val fragmentPagerAdapter = AnimeDetailFragmentPagerAdapter(
            this,
            partialAnime,
            supportFragmentManager
        )
        val viewPager = findViewById<ViewPager>(R.id.anime_view_pager)
        viewPager.adapter = fragmentPagerAdapter
        val tabs = findViewById<TabLayout>(R.id.tabs_anime)
        tabs.setupWithViewPager(viewPager)

        //setting the content
        val imageBg = findViewById<ImageView>(R.id.anime_bg_collapsing_image)
        val animeTitle = findViewById<TextView>(R.id.anime_bg_title)
        val animeSubtitle = findViewById<TextView>(R.id.anime_bg_subtitle)
        val animeScore = findViewById<TextView>(R.id.anime_bg_score)

        fetchAnime(fragmentPagerAdapter, id, imageBg, animeTitle, animeSubtitle, animeScore)
        fetchVideo(fragmentPagerAdapter, partialAnime)
        fetchRecs(fragmentPagerAdapter, partialAnime)
    }

    private fun fetchAnime(fpa: AnimeDetailFragmentPagerAdapter, id: Int, imageBg: ImageView, animeTitle: TextView, animeSubtitle: TextView, animeScore: TextView) {
        AsyncFetcherSetter({
            JikanCacheHandler.getAnime(id)
        }, {
            setActivityData(it, imageBg, animeTitle, animeSubtitle, animeScore)
            fpa.setAnime(it)
        }).execute()
    }

    private fun fetchVideo(fpa: AnimeDetailFragmentPagerAdapter, partialAnime: Anime) {
        AsyncFetcherSetter({
            JikanCacheHandler.getVideo(partialAnime)
        }, {
            fpa.setEpisodes(it)
        }).execute()
    }

    private fun fetchRecs(fpa: AnimeDetailFragmentPagerAdapter, partialAnime: Anime) {
        AsyncFetcherSetter({
            JikanCacheHandler.getRecommendationPage(partialAnime)
        }, {
            fpa.setRecs(it)
        }).execute()
    }
    private fun setActivityData(anime: Anime, imageBg: ImageView, animeTitle: TextView, animeSubtitle: TextView, animeScore: TextView) {
        ImageHandler.getInstance(this).load(R.drawable.neko).placeholder(R.drawable.neko).into(imageBg)
        GoogleSearchAsyncTask(imageBg,applicationContext).execute(anime.title)

        animeTitle.text = anime.title
        animeSubtitle.text = getGenres(anime)
        animeScore.text = anime.score.toString()
    }

    private fun getGenres(anime: Anime): String {
        val genres = anime.genres ?: return "Loading..."
        var genresText = ""
        for ((count, genre) in genres.withIndex()){
            genresText += if (count+1 >= genres.size) {
                genre.name + "."
            } else {
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