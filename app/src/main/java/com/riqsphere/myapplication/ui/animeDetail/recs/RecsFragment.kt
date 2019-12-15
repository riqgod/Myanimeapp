package com.riqsphere.myapplication.ui.animeDetail.recs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.doomsdayrs.jikan4java.types.support.recommendations.RecommendationPage
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.model.search.SearchModel
import com.riqsphere.myapplication.room.MyaaViewModel
import com.riqsphere.myapplication.ui.discover.search.SearchAdapter

class RecsFragment : Fragment(){

    private lateinit var list: ListView
    private lateinit var adapter: SearchAdapter
    private var dataToSet: RecommendationPage? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recs, container, false)
        //locate the list view in recs
            list = view.findViewById(R.id.recs_listview)

        val myaaViewModel = MyaaViewModel(activity!!.application)
        adapter = SearchAdapter(activity!!, myaaViewModel)

        //binds the adapter to the list view
        list.adapter = adapter

        observeWatchlist(myaaViewModel)
        dataToSet?.let { setRecs(it) }

        return view
    }

    private fun observeWatchlist(myaaViewModel: MyaaViewModel) {
        myaaViewModel.allWatchlistAnime.observe(this, Observer {
            adapter.setWatchlistData(it)
        })
    }

    fun setRecs(recommendationPage: RecommendationPage) {
        dataToSet = recommendationPage
        if(::adapter.isInitialized) {
            adapter.setData(SearchModel.arrayListFromRecommend(recommendationPage.recommends))
        }
    }
}