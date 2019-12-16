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

<<<<<<< HEAD
    private lateinit var myaaViewModel: MyaaViewModel
     private var TAG = "google api cse"
    private var id = -1
    private lateinit var fragmentPagerAdapter: AnimeDetailFragmentPagerAdapter

    private lateinit var partialAnime: Anime

    private var segredo:String = "SmNkssGijWOK4"
    private var q = "h1c6L4PR77BLIFUv"
    private lateinit var viewPager: ViewPager
    private lateinit var tabs: TabLayout
    public lateinit var imageBg: ImageView
    private lateinit var animeTitle: TextView
    private lateinit var animeSubtitle: TextView
    private lateinit var animeScore: TextView

=======
>>>>>>> 7567bf1685142eea68fc8fc078f9a3e74a0e7e7b
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
        ImageHandler.getInstance(this).load(R.drawable.neko).into(imageBg)

<<<<<<< HEAD
    private fun setActivityData(anime: Anime) {

        val foin:String = "AIzaSyAcr"
        val dale:String = "01394810328760818055"
        val ahn= "8:qacwtjfwy55"

        var animeSearch:String = anime.title+""
        animeSearch =  animeSearch.replace(" ", "+");

        var urlString:String = "https://www.googleapis.com/customsearch/v1?q="+animeSearch+"&cx="+dale+ahn+"&imgSize=huge&num=1&searchType=image&key="+foin+q+segredo;

                var url:URL? = null;
                try {
                    url = URL(urlString);
                } catch (e: MalformedURLException) {
                    Log.e(TAG, "ERROR converting String to URL " + e.toString());
                }
                Log.d(TAG, "Url = "+  urlString);

        // start AsyncTask
        val searchTask:GoogleSearchAsyncTask = GoogleSearchAsyncTask(imageBg,applicationContext);
        searchTask.execute(url);
=======
        GoogleSearchAsyncTask(imageBg,applicationContext).execute(anime.title)
>>>>>>> 7567bf1685142eea68fc8fc078f9a3e74a0e7e7b

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