package com.riqsphere.myapplication.ui.animeDetail

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
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
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

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
        ImageHandler.getInstance(this).load(R.drawable.dororo).into(imageBg)
        val apiKey = "AIzaSyCDygTKwzesUMdO0QNbnGWbNGJ7PM9Pnpc"
        val keyCse = "013948103287608180558:zyl5sbvqbkk"

        var animeSearch:String = anime.title+""
        animeSearch =  animeSearch.replace(" ", "+")

        val urlString = "https://www.googleapis.com/customsearch/v1?q=$animeSearch&cx=$keyCse&imgSize=huge&num=1&searchType=image&key=$apiKey"

        var url:URL? = null
        try {
            url = URL(urlString)
        } catch (e: MalformedURLException) {
        }

        GoogleSearchAsyncTask(imageBg,applicationContext).execute(url)

        animeTitle.text = anime.title
        animeSubtitle.text = getGenres(anime)
        animeScore.text = anime.score.toString()
    }

     private class GoogleSearchAsyncTask(private val imageBg: ImageView, private val context: Context) : AsyncTask<URL, Integer, String>() {
        @Override
        override fun doInBackground(vararg urls: URL?): String? {

            val url = urls[0]
            val TAG = "google api cse"
            Log.d(TAG, "AsyncTask - doInBackground, url=$url")

            // Http connection
            var conn: HttpURLConnection? = null
            try {
                conn = url!!.openConnection() as HttpURLConnection?
            } catch (e: IOException) {
                Log.e(TAG, "Http connection ERROR $e")
            }
            var responseCode:Int = 0
            var responseMessage:String = ""
            try {
               responseCode = conn!!.getResponseCode()
               responseMessage = conn!!.getResponseMessage()
            } catch (e: IOException) {
                Log.e(TAG, "Http getting response code ERROR $e")
            }

            Log.d(TAG, "Http response code =$responseCode message=$responseMessage")

            try {

                if(responseCode == 200) {
                    val rd = BufferedReader(InputStreamReader(conn!!.inputStream))
                    val sb: StringBuilder = StringBuilder()
                    var line:String? = rd.readLine()

                    while ( line != null) {
                        sb.append(line + "\n")
                        line = rd.readLine()
                    }
                    rd.close()

                    conn.disconnect()

                    return sb.toString()
                    
                }else{
                    val errorMsg =
                        "Http ERROR response $responseMessage\nMake sure to replace in code your own Google API key and Search Engine ID"
                    Log.e(TAG, errorMsg)
                    val result = errorMsg
                    return  result
                }
            } catch (e: IOException ) {
                Log.e(TAG, "Http Response ERROR $e")
            }


            return null
        }

        override fun onPostExecute(result:String) {
            var result2 = parseJSon(result)
            Log.d("image url download",result2)
            ImageHandler.getInstance(context).load(result2).into(imageBg)

        }

        fun parseJSon(string:String):String{
            var processedString = string.substringAfter("items")
            processedString = processedString.substringAfter("\"link\": \"")
            processedString = processedString.substringBefore("\"")
            processedString.substringBefore("\"")
            return processedString
        }

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