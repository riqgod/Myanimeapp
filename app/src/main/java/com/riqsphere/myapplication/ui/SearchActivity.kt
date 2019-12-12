package com.riqsphere.myapplication.ui


import android.app.Activity
import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import com.github.doomsdayrs.jikan4java.core.search.animemanga.AnimeSearch
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime
import com.github.doomsdayrs.jikan4java.types.main.anime.animePage.AnimePageAnime
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.cache.JikanCacheHandler
import com.riqsphere.myapplication.model.search.SearchModel
import com.riqsphere.myapplication.ui.search.SearchAdapter


class SearchActivity : Activity(), SearchView.OnQueryTextListener{

    private lateinit var list:ListView
    private lateinit var adapter:SearchAdapter
    private lateinit var model:ArrayList<SearchModel>
    private lateinit var editsearch:SearchView
    private lateinit var resultText:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)

        //generate sample data

        model = insertList(requestDealer())


        //passing the ui element
        resultText = findViewById(R.id.search_text2)
        val getSearchText = intent.getStringExtra("searchText")
        resultText.setText(getSearchText)


        //locate the listview in listview_main.xml
        list = findViewById<ListView>(R.id.search_list)

        //pass results to ListViewAdapter
        adapter = SearchAdapter()
        adapter.setData(model,this)

        //binds the adapter to the list view
        list.adapter = adapter

        //locate the edit text in listview_main.xml

        editsearch = findViewById<SearchView>(R.id.search_view)
        val searchInput = intent.getStringExtra("searchInput")
        editsearch.setQuery(searchInput,false)
        editsearch.isFocusable = true
        editsearch.isIconified = false
        editsearch.isIconifiedByDefault = false

        /*
        editsearch.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                editsearch.clearFocus()
            }
        }) */


        editsearch.setOnQueryTextListener(this);



    }

    private fun requestDealer(): ArrayList<AnimePageAnime> {

        //for now just do a search
        val searchInput = intent.getStringExtra("searchInput")

        return searchRequest(searchInput)
    }

    fun temporaryFetch(id:Int): Anime {
        return JikanCacheHandler.getAnime(id)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            showResults(query)
        }
    }

    private fun showResults(query: String) {
        // Query your data set and show results
        // ...
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        val query = p0
        //do here the part that search in the mal
        val result = searchRequest(query)
        var model = insertList(result)
        adapter.setData(model,this)
        resultText.setText("Results:")

        return false;
    }

    private fun insertList(result: ArrayList<AnimePageAnime>): ArrayList<SearchModel> {
        var model = ArrayList<SearchModel>(0)
        for (i in result) {
            model.add(SearchModel(i))
        }
        return model
    }

    private fun searchRequest(query: String?) = AnimeSearch().setQuery(query).get().get().animes

    override fun onQueryTextChange(p0: String?): Boolean {
        return false;
    }
}