package com.riqsphere.myapplication.ui.animeDetail.recs

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.doomsdayrs.jikan4java.types.support.recommendations.RecommendationPage
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.room.MyaaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class RecsFragment : Fragment(){

    private var dataToSet: RecommendationPage? = null
    private var viewAdapter: RecsAdapter? = null

    private var rv: RecyclerView? = null
    private var noRecsText: TextView? = null
    private var pb: ProgressBar? = null

    private var firstRun = true
    private var dataHasBeenSet = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recs, container, false)

        if (firstRun) {
            val myaaViewModel = MyaaViewModel(activity!!.application)
            viewAdapter = RecsAdapter(activity!!, myaaViewModel)
            observeWatchlist(myaaViewModel, viewAdapter!!)
            firstRun = false
        }

        rv = view.findViewById(R.id.recs_rv)
        noRecsText = view.findViewById(R.id.recs_no_recs)
        noRecsText?.visibility = View.GONE
        pb = view.findViewById(R.id.recs_pb)

        if (!dataHasBeenSet) {
            dataToSet?.let { setRecs(it) }
        } else {
            if (dataToSet?.recommends?.isNotEmpty() == true) {
                pb?.visibility = View.GONE
            }
        }

        return view
    }

    override fun onStart() {
        super.onStart()

        val viewManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv?.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    private class AsyncSetAdapter(private val rv: RecyclerView?, private val adapter: RecsAdapter?): AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? = runBlocking {
            delay(400)
            withContext(Dispatchers.Main) { rv?.adapter = adapter }
            null
        }
    }

    private fun someRecs() {
        pb?.visibility = View.GONE
        noRecsText?.visibility = View.GONE
    }

    private fun noRecs() {
        pb?.visibility = View.GONE
        noRecsText?.visibility = View.VISIBLE
    }

    private fun observeWatchlist(myaaViewModel: MyaaViewModel, viewAdapter: RecsAdapter) {
        myaaViewModel.allWatchlistAnime.observe(this, Observer {
            viewAdapter.setWatchlistData(it)
        })
    }

    fun setRecs(recommendationPage: RecommendationPage) {
        dataToSet = recommendationPage
        viewAdapter?.let { dataHasBeenSet = true }
        viewAdapter?.setData(RecsModel.arrayListFromRecommend(recommendationPage.recommends))

        if (recommendationPage.recommends.isEmpty()) {
            noRecs()
        } else {
            someRecs()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        release()
    }

    override fun onDestroy() {
        super.onDestroy()
        release()
    }

    override fun onDetach() {
        super.onDetach()
        release()
    }

    private fun release() {
        rv = null
        noRecsText = null
        pb = null
    }
}