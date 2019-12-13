package com.riqsphere.myapplication.ui.discover

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.cache.JikanCacheHandler
import com.riqsphere.myapplication.model.recommendations.Recommendation
import com.riqsphere.myapplication.model.search.SearchModel
import com.riqsphere.myapplication.model.watchlist.WatchlistAnime
import com.riqsphere.myapplication.room.MyaaViewModel
import com.riqsphere.myapplication.ui.SearchActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class DiscoverFragment : Fragment() {

    private lateinit var rootView: View
    private lateinit var viewPool: RecyclerView.RecycledViewPool
    private lateinit var myaaViewModel: MyaaViewModel
    private lateinit var allWatchlistAnime: LiveData<List<WatchlistAnime>>
    private lateinit var allRecommendation: LiveData<List<Recommendation>>
    private lateinit var editTxt:EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView =  inflater.inflate(R.layout.fragment_discover, container, false)
        if (activity == null) {
            return rootView
        }

        viewPool = RecyclerView.RecycledViewPool()
        myaaViewModel = MyaaViewModel(activity!!.application)
        allWatchlistAnime = myaaViewModel.allWatchlistAnime
        allRecommendation = myaaViewModel.allRecommendation
        setRecyclerViews()

        editTxt = rootView.findViewById(R.id.search_input)
        editTxt.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                goToSearchPage("Results:")
                return@OnEditorActionListener true
            }
            false
        })
        editTxt.isFocusable=true

        val button: ImageButton = rootView.findViewById(R.id.search_img_btn1)
        button.setOnClickListener {
            editTxt.requestFocus()
        }

        return rootView
    }

    private fun goToSearchPage(searchText: String) {
        val i = Intent(activity, SearchActivity::class.java)
        i.putExtra("searchInput",editTxt.text.toString())
        i.putExtra("searchText",searchText)
        startActivity(i)
    }

    private fun setRecyclerViews() {
        val empty = { arrayListOf (
            SearchModel(JikanCacheHandler.getAnime(27)),
            SearchModel(JikanCacheHandler.getAnime(20)),
            SearchModel(JikanCacheHandler.getAnime(21)),
            SearchModel(JikanCacheHandler.getAnime(22)),
            SearchModel(JikanCacheHandler.getAnime(23)),
            SearchModel(JikanCacheHandler.getAnime(24))
        )}

        val top4uRecyclerView = initializeRecommendationsRecyclerView(R.id.dc_rv_top4u)
        observeRecommendation(top4uRecyclerView)

        val seasonalRecyclerView = initializeDiscoverRecyclerView(R.id.dc_rv_seasonal)
        val seasonalPair = Pair(seasonalRecyclerView, fetchCurrentSeason)

        val topUpcomingRecyclerView = initializeDiscoverRecyclerView(R.id.dc_rv_top_upcoming)
        val topUpcomingPair = Pair(topUpcomingRecyclerView, fetchTopUpcoming)

        val mostPoplarRecyclerView = initializeDiscoverRecyclerView(R.id.dc_rv_mp)
        val mostPoplarPair = Pair(mostPoplarRecyclerView, fetchMostPoplar)

        val topScoreRecyclerView = initializeDiscoverRecyclerView(R.id.dc_rv_score)
        val topScorePair = Pair(topScoreRecyclerView, fetchTopScore)

        val pairs = arrayOf(seasonalPair, topUpcomingPair, mostPoplarPair, topScorePair)
        AsyncDataSetter().execute(*pairs)

        val recyclers = arrayOf(top4uRecyclerView, seasonalRecyclerView, topUpcomingRecyclerView, mostPoplarRecyclerView, topScoreRecyclerView)
        observeWatchlist(*recyclers)
    }

    private val fetchCurrentSeason = {
        SearchModel.arrayListFromSeasonSearch(JikanCacheHandler.getCurrentSeason())
    }

    private val fetchTopUpcoming = {
        SearchModel.arrayListFromTopListing(JikanCacheHandler.getTopUpcoming())
    }

    private val fetchMostPoplar = {
        SearchModel.arrayListFromAnimePageAnime(JikanCacheHandler.getMostPoplar())
    }

    private val fetchTopScore = {
        SearchModel.arrayListFromAnimePageAnime(JikanCacheHandler.getTopScore())
    }

    private fun initializeRecommendationsRecyclerView(recyclerViewID: Int): RecyclerView {
        val adapt = DiscoverRecommendationAdapter(activity!!)
        val manager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val rv = rootView.findViewById<RecyclerView>(recyclerViewID)
        rv.apply {
            layoutManager = manager
            adapter = adapt
            setRecycledViewPool(viewPool)
        }
        return rv
    }

    private fun initializeDiscoverRecyclerView(recyclerViewID: Int): RecyclerView {
        val adapt = DiscoverAdapter(activity!!)
        val manager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val rv = rootView.findViewById<RecyclerView>(recyclerViewID)
        rv.apply {
            layoutManager = manager
            adapter = adapt
            setRecycledViewPool(viewPool)
        }
        return rv
    }

    private fun observeRecommendation(vararg params: RecyclerView) {
        params.forEach { recyclerView ->
            allRecommendation.observe(this, Observer {
                when {
                    (recyclerView.adapter is DiscoverRecommendationAdapter) -> {
                        val adapt = recyclerView.adapter as DiscoverRecommendationAdapter
                        adapt.setRecListData(it)
                    }
                }
            })
        }
    }

    private fun observeWatchlist(vararg params: RecyclerView) {
        params.forEach { recyclerView ->
            allWatchlistAnime.observe(this, Observer {
                when {
                    (recyclerView.adapter is DiscoverAdapter) -> {
                        val adapt = recyclerView.adapter as DiscoverAdapter
                        adapt.setWatchlistData(it)
                    }
                    (recyclerView.adapter is DiscoverRecommendationAdapter) -> {
                        val adapt = recyclerView.adapter as DiscoverRecommendationAdapter
                        adapt.setWatchlistData(it)
                    }
                }
            })
        }
    }

    private class AsyncDataSetter: AsyncTask<Pair<RecyclerView, () -> ArrayList<SearchModel>>, Void, Void>() {
        override fun doInBackground(vararg params: Pair<RecyclerView, () -> ArrayList<SearchModel>>?): Void? {
            runBlocking {
                params.forEach { pair ->
                    launch {
                        if (pair != null) {
                            val recyclerView = pair.first
                            val data = withContext(Dispatchers.Unconfined) { fetchData(pair.second) }
                            recyclerView.setData(data)
                        }
                    }
                }
            }
            return null
        }

        private inline fun <T> fetchData(func: () -> T) = func()

        private suspend fun RecyclerView.setData(data: ArrayList<SearchModel>) {
            if (adapter is DiscoverAdapter) {
                val discoverAdapter = adapter as DiscoverAdapter
                withContext(Dispatchers.Main) {
                    discoverAdapter.setSearchData(data)
                }
            }
        }
    }
}