package com.riqsphere.myapplication.ui.animeDetail

import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime
import com.google.android.material.tabs.TabLayout
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.cache.JikanCacheHandler
import com.riqsphere.myapplication.model.watchlist.WatchlistAnime
import com.riqsphere.myapplication.room.MyaaViewModel
import com.riqsphere.myapplication.utils.ImageHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class AnimeDetailActivity : AppCompatActivity() {

    private var floatingButton: View? = null
    private var toolbar: Toolbar? = null
    private var progressBar: ProgressBar? = null

    private var myaaViewModel: MyaaViewModel? = null

    private var added = false

    private var watchlistObserved = false
    private var fetchedAnime: Anime? = null

    private var id = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.anime_detail_activity)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.hide()

        id = intent.getIntExtra("id", -1)
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
        floatingButton = findViewById(R.id.floatingButton)
        floatingButton?.visibility = View.GONE

        myaaViewModel = MyaaViewModel(application!!)

        progressBar = findViewById<ProgressBar>(R.id.anime_bg_progress_bar)
        observe(imageBg, animeTitle)

        fetchAnime(fragmentPagerAdapter, id, imageBg, animeTitle, animeSubtitle, animeScore)
        fetchVideo(fragmentPagerAdapter, partialAnime)
        fetchRecs(fragmentPagerAdapter, partialAnime)
    }

    private fun observe(imageBg: ImageView, animeTitle: TextView) {
        myaaViewModel?.allWatchlistAnime?.observe(this, Observer {
            watchlistObserved = true
            val wa = it.firstOrNull { wa -> wa.id == id }
            added = wa != null
            if (added) {
                setPartialActivityData(wa!!, imageBg, animeTitle)
                progressBar?.max = wa.episodes
                progressBar?.progress = wa.episodesWatched.size
                showRemoveButton()
            } else if (fetchedAnime != null) {
                showAddButton()
            }
        })
    }

    private fun showAddButton() {
        floatingButton?.visibility = View.VISIBLE
        supportActionBar?.hide()
    }

    private fun showRemoveButton() {
        floatingButton?.visibility = View.GONE
        supportActionBar?.show()
    }

    private fun setPartialActivityData(
        watchlistAnime: WatchlistAnime,
        imageBg: ImageView,
        animeTitle: TextView
    ) {
        ImageHandler.getInstance(this).load(R.drawable.neko2).placeholder(R.drawable.neko2).into(imageBg)
        loadImageBg(imageBg, watchlistAnime.title)

        animeTitle.text = watchlistAnime.title
    }

    private fun setActivityData(anime: Anime, imageBg: ImageView, animeTitle: TextView, animeSubtitle: TextView, animeScore: TextView) {
        fetchedAnime = anime
        progressBar?.max = anime.episodes
        if (watchlistObserved && !added) {
            showAddButton()
        }

        ImageHandler.getInstance(this).load(R.drawable.neko2).placeholder(R.drawable.neko2).into(imageBg)
        loadImageBg(imageBg, anime.title)

        animeTitle.text = anime.title
        animeSubtitle.text = getGenres(anime)
        animeScore.text = anime.score.toString()
    }

    private fun fetchAnime(
        fpa: AnimeDetailFragmentPagerAdapter,
        id: Int,
        imageBg: ImageView,
        animeTitle: TextView,
        animeSubtitle: TextView,
        animeScore: TextView
    ) {
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

    private var hasLoaded = false
    private fun loadImageBg(imageBg: ImageView, title: String) {
        if (!hasLoaded) {
            GoogleSearchAsyncTask(imageBg, applicationContext).execute(title)
            hasLoaded = true
        }
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

    fun floatingAddButton(view: View){
        //disable this button
        if(!added){
            //change the text and icon

            val text = this.findViewById<TextView>(R.id.floating_text)
            val icon = this.findViewById<ImageView>(R.id.floating_add)

            text.text = "ADDED TO WATCH LIST"
            icon.setImageResource(R.drawable.ic_addedsimple_added)

            //add to the watchlist (call the real add watchlist function)
            myaaViewModel?.insert(fetchedAnime!!)

            //disappear in 2 seg
            val handler = Handler()
            val floatingButton = this.findViewById<ConstraintLayout>(R.id.floatingButton)

            handler.postDelayed({
                disappear(floatingButton,text,icon)
                //disable floatingButton
                //when disapear change to the text and icon back
                //when enable this button
            },2000L)
        }
    }

    private fun disappear(floatingButton:ConstraintLayout, text:TextView, icon:ImageView){
        //disappear button
        floatingButton.visibility = View.GONE
        supportActionBar?.show() // showing the menu, so, remove option be able to click

        //change back the content, reset
        text.text = "ADD TO WATCH LIST"
        icon.setImageResource(R.drawable.ic_add_1simple_add)

        //enabling btn, reset
        added = false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean{
        menuInflater.inflate(R.menu.menu_anime_detail,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.remove && id > 0){
            //hide the toolbar
            supportActionBar?.hide()
            //enable the add button again
            val floatingButton = this.findViewById<ConstraintLayout>(R.id.floatingButton)
            floatingButton.visibility = View.VISIBLE

            //call the method the remove anime form the watchlist method
            myaaViewModel?.delete(id)
            added = false

            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        floatingButton = null
        toolbar = null
        progressBar = null
    }
}