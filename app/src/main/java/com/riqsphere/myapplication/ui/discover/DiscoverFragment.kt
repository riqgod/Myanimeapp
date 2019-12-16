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
import com.riqsphere.myapplication.ui.discover.search.SearchActivity
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
        editTxt.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                goToSearchPage()
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

    private fun goToSearchPage() {
        val i = Intent(activity, SearchActivity::class.java)
        i.putExtra("searchInput", editTxt.text.toString())
        i.putExtra("searchText", "Results:")
        startActivity(i)
    }

    private fun setRecyclerViews() {
        val top4uRecyclerView = initializeRecommendationsRecyclerView(R.id.dc_rv_top4u)
        val top4uLoadingView = rootView.findViewById<View>(R.id.dc_loading_top4u)
        observeRecommendation(Pair(top4uRecyclerView, top4uLoadingView))

        val seasonalRecyclerView = initializeDiscoverRecyclerView(R.id.dc_rv_seasonal)
        val seasonalLoadingView = rootView.findViewById<View>(R.id.dc_loading_seasonal)
        val seasonalTriple = Triple(seasonalRecyclerView, seasonalLoadingView, fetchCurrentSeason)

        val topUpcomingRecyclerView = initializeDiscoverRecyclerView(R.id.dc_rv_top_upcoming)
        val topUpcomingLoadingView = rootView.findViewById<View>(R.id.dc_loading_top_upcoming)
        val topUpcomingTriple = Triple(topUpcomingRecyclerView, topUpcomingLoadingView, fetchTopUpcoming)

        val mostPoplarRecyclerView = initializeDiscoverRecyclerView(R.id.dc_rv_mp)
        val mostPoplarLoadingView = rootView.findViewById<View>(R.id.dc_loading_mp)
        val mostPoplarTriple = Triple(mostPoplarRecyclerView, mostPoplarLoadingView, fetchMostPoplar)

        val topScoreRecyclerView = initializeDiscoverRecyclerView(R.id.dc_rv_score)
        val topScoreLoadingView = rootView.findViewById<View>(R.id.dc_loading_score)
        val topScoreTriple = Triple(topScoreRecyclerView, topScoreLoadingView, fetchTopScore)

        val triples = arrayOf(seasonalTriple, topUpcomingTriple, mostPoplarTriple, topScoreTriple)
        AsyncDataSetter().execute(*triples)

        val recyclers = arrayOf(top4uRecyclerView, seasonalRecyclerView, topUpcomingRecyclerView, mostPoplarRecyclerView, topScoreRecyclerView)
        observeWatchlist(*recyclers)
    }

    private val fetchCurrentSeason = {
        SearchModel.arrayListFromSeasonSearch(JikanCacheHandler.getCurrentSeason().animes)
    }

    private val fetchTopUpcoming = {
        SearchModel.arrayListFromTopListing(JikanCacheHandler.getTopUpcoming().topListings ?: arrayListOf())
    }

    private val fetchMostPoplar = {
        SearchModel.arrayListFromAnimePageAnime(JikanCacheHandler.getMostPoplar().animes)
    }

    private val fetchTopScore = {
        SearchModel.arrayListFromAnimePageAnime(JikanCacheHandler.getTopScore().animes)
    }

    private fun initializeRecommendationsRecyclerView(recyclerViewID: Int): RecyclerView {
        val adapt = DiscoverRecommendationAdapter(activity!!, myaaViewModel)
        val manager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val rv = rootView.findViewById<RecyclerView>(recyclerViewID)
        rv.apply {
            layoutManager = manager
            adapter = adapt
        }
        return rv
    }

    private fun initializeDiscoverRecyclerView(recyclerViewID: Int): RecyclerView {
        val adapt = DiscoverAdapter(activity!!, myaaViewModel)
        val manager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val rv = rootView.findViewById<RecyclerView>(recyclerViewID)
        rv.apply {
            layoutManager = manager
            adapter = adapt
            setRecycledViewPool(viewPool)
        }
        return rv
    }

    private fun observeRecommendation(vararg params: Pair<RecyclerView, View>) {
        val draParams = params.mapNotNull {
            val recyclerView = it.first
            if (recyclerView.adapter is DiscoverRecommendationAdapter) {
                Pair(recyclerView.adapter as DiscoverRecommendationAdapter, it.second)
            } else {
                null
            }
        }
        allRecommendation.observe(this, Observer {
            draParams.forEach { pair ->
                val dra = pair.first
                val loadingView = pair.second
                dra.setRecListData(it)
                loadingView.visibility = View.GONE
            }
        })
    }

    private fun observeWatchlist(vararg params: RecyclerView) {
        val draParams = params.mapNotNull {
            if (it.adapter is DiscoverRecommendationAdapter) {
                it.adapter as DiscoverRecommendationAdapter
            } else {
                null
            }
        }

        val daParams = params.mapNotNull {
            if (it.adapter is DiscoverAdapter) {
                it.adapter as DiscoverAdapter
            } else {
                null
            }
        }
        allWatchlistAnime.observe(this, Observer {
            draParams.forEach { dra ->
                dra.setWatchlistData(it)
            }
            daParams.forEach { da ->
                da.setWatchlistData(it)
            }
        })
    }

    private class AsyncDataSetter: AsyncTask<Triple<RecyclerView, View, () -> ArrayList<SearchModel>>, Void, Void>() {
        override fun doInBackground(vararg params: Triple<RecyclerView, View, () -> ArrayList<SearchModel>>?): Void? {
            runBlocking {
                params.forEach { triple ->
                    launch {
                        if (triple != null) {
                            val recyclerView = triple.first
                            val loadingView = triple.second
                            val data = withContext(Dispatchers.Unconfined) { fetchData(triple.third) }
                            withContext(Dispatchers.Main) {
                                recyclerView.setData(data)
                                loadingView.visibility = View.GONE
                            }
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