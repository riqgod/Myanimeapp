package com.riqsphere.myapplication.ui.animeDetail

import android.content.Context
import android.os.AsyncTask
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class AnimeDetailActivity : AppCompatActivity() {

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

        animeTitle.text = anime.title
        animeSubtitle.text = getGenres(anime)
        animeScore.text = anime.score.toString()
    }

     private class GoogleSearchAsyncTask(var imageBg:ImageView, var context:Context) : AsyncTask<URL, Integer, String>() {

         /*
        protected override fun onPreExecute(){
            Log.d(TAG, "AsyncTask - onPreExecute");
            // show progressbar
            progressBar.setVisibility(View.VISIBLE);
        }
            */

        @Override
        protected override fun doInBackground(vararg urls: URL?): String {

            val url = urls[0];
            val TAG = "google api cse"
            Log.d(TAG, "AsyncTask - doInBackground, url=" + url);

            // Http connection
            var conn: HttpURLConnection? = null;
            try {
                conn = url!!.openConnection() as HttpURLConnection?
            } catch (e: IOException) {
                Log.e(TAG, "Http connection ERROR " + e.toString());
            }
            var responseCode:Int = 0
            var responseMessage:String = ""
            try {
               responseCode = conn!!.getResponseCode();
               responseMessage = conn!!.getResponseMessage();
            } catch (e: IOException) {
                Log.e(TAG, "Http getting response code ERROR " + e.toString());
            }

            Log.d(TAG, "Http response code =" + responseCode + " message=" + responseMessage);

            try {

                if(responseCode == 200) {

                    // response OK

                    var rd:BufferedReader = BufferedReader(InputStreamReader(conn!!.getInputStream()));
                    var sb: StringBuilder = StringBuilder();
                    var line:String? = rd.readLine();

                    while ( line != null) {
                        sb.append(line + "\n");
                        line = rd.readLine();
                    }
                    rd.close();

                    conn.disconnect();

                    var result = sb.toString();

                    return result;

                }else{

                    // response problem

                    var errorMsg = "Http ERROR response " + responseMessage + "\n" + "Make sure to replace in code your own Google API key and Search Engine ID";
                    Log.e(TAG, errorMsg);
                    var result = errorMsg;
                    return  result;

                }
            } catch (e: IOException ) {
                Log.e(TAG, "Http Response ERROR " + e.toString());
            }


            return "";
        }

        /*
        protected fun onProgressUpdate(Integer... progress) {
            Log.d(TAG, "AsyncTask - onProgressUpdate, progress=" + progress);

        }
        */
        protected override fun onPostExecute(result:String) {

            //Log.d("funfou google api cse", result);

            // hide progressbar
           // progressBar.setVisibility(View.GONE);
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