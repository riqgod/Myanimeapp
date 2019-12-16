package com.riqsphere.myapplication.ui.animeDetail.recs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.doomsdayrs.jikan4java.types.support.recommendations.RecommendationPage
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.room.MyaaViewModel

class RecsFragment : Fragment(){

    private var dataToSet: RecommendationPage? = null
    private lateinit var viewAdapter: RecsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recs, container, false)

        val myaaViewModel = MyaaViewModel(activity!!.application)

        viewAdapter = RecsAdapter(activity!!, myaaViewModel)
        val viewManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        view.findViewById<RecyclerView>(R.id.recs_rv).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        observeWatchlist(myaaViewModel, viewAdapter)
        dataToSet?.let { setRecs(it) }

        return view
    }

    private fun observeWatchlist(myaaViewModel: MyaaViewModel, viewAdapter: RecsAdapter) {
        myaaViewModel.allWatchlistAnime.observe(this, Observer {
            viewAdapter.setWatchlistData(it)
        })
    }

    fun setRecs(recommendationPage: RecommendationPage) {
        dataToSet = recommendationPage
        if(::viewAdapter.isInitialized) {
            viewAdapter.setData(RecsModel.arrayListFromRecommend(recommendationPage.recommends))
        }
    }
}