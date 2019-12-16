package com.riqsphere.myapplication.ui.discover.search


import android.app.SearchManager
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.github.doomsdayrs.jikan4java.types.main.anime.animePage.AnimePage
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.cache.JikanCacheHandler
import com.riqsphere.myapplication.model.search.SearchModel
import com.riqsphere.myapplication.room.MyaaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class SearchActivity: AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var list: ListView
    private lateinit var adapter: SearchAdapter
    private lateinit var editSearch: SearchView
    private lateinit var resultText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)

        val myaaViewModel = MyaaViewModel(application!!).apply {
            allWatchlistAnime.observe(this@SearchActivity, Observer {
                adapter.setWatchlistData(it)
            })
        }

        resultText = findViewById(R.id.search_text2)
        resultText.text = intent.getStringExtra("searchText")

        list = findViewById(R.id.search_list)
        adapter = SearchAdapter(this, myaaViewModel)
        list.adapter = adapter

        editSearch = findViewById(R.id.search_view)
        val searchInput = intent.getStringExtra("searchInput")
        editSearch.setQuery(searchInput,false)
        editSearch.isFocusable = true
        editSearch.isIconified = false
        editSearch.isIconifiedByDefault = false

        onQueryTextSubmit(editSearch.query.toString())
        editSearch.setOnQueryTextListener(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            performSearch(query)
        }
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        performSearch(query)
        return false
    }

    private fun performSearch(query: String?) {
        val callback = { animePage: AnimePage ->
            showResults(animePage)
        }
        AsyncSearch(query ?: "", callback).execute()
    }

    private class AsyncSearch(private val query: String, private val callback: (AnimePage) -> Unit): AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? = runBlocking {
            val searchResult = withContext(Dispatchers.Unconfined) {
                JikanCacheHandler.search(query)
            }
            withContext(Dispatchers.Main){ callback(searchResult) }
            null
        }
    }

    private fun showResults(animePage: AnimePage) {
        val arr = animePage.animes ?: arrayListOf()
        adapter.setData(SearchModel.arrayListFromAnimePageAnime(arr))
        resultText.text = "Results:"
    }
}