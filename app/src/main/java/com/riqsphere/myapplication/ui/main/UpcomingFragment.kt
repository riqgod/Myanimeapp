package com.riqsphere.myapplication.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.doomsdayrs.jikan4java.core.Connector
import com.github.doomsdayrs.jikan4java.types.main.schedule.Schedule
import com.github.doomsdayrs.jikan4java.types.main.schedule.SubAnime
import com.riqsphere.myapplication.MainActivity

import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.utils.addWeekday
import com.riqsphere.myapplication.utils.nthWeekday
import com.riqsphere.myapplication.upcoming.UpcomingAdapter
import com.riqsphere.myapplication.upcoming.UpcomingModel
import com.riqsphere.myapplication.watchlist.room.WatchlistAnime
import com.riqsphere.myapplication.watchlist.room.WatchlistViewModel
import java.util.*
import kotlin.collections.ArrayList

class UpcomingFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: UpcomingAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var watchlistViewModel: WatchlistViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        watchlistViewModel = WatchlistViewModel(this.activity!!.application)
        watchlistViewModel.allWatchlistAnime.observe(this, androidx.lifecycle.Observer {
            it?.let {
                val upcoming = fetchUpcoming(it)
                viewAdapter.setData(upcoming)
            }
        })

        val view = inflater.inflate(R.layout.fragment_upcoming, container, false)
        val upcomingAdapter = UpcomingAdapter(activity!!.applicationContext)

        viewManager = LinearLayoutManager(activity)
        viewAdapter = upcomingAdapter
        recyclerView = view.findViewById<RecyclerView>(R.id.uc_rv).apply {
            layoutManager = viewManager
            setHasFixedSize(true)
            adapter = viewAdapter
            addItemDecoration(DividerItemDecoration(activity,DividerItemDecoration.VERTICAL))
        }

        return view
    }

    private fun fetchUpcoming(allWatchlistAnime: List<WatchlistAnime>): ArrayList<UpcomingModel> {
        val schedule = Connector().currentSchedule.get() //TODO: parallelize this
        val upcomingPairs = filteredUpcoming(schedule, allWatchlistAnime)
        return upcomingPairs.mapTo(ArrayList()) {
            UpcomingModel(it.first, it.second)
        }
    }


    private fun filteredUpcoming(schedule: Schedule, watchlistAnimes: List<WatchlistAnime>): ArrayList<Pair<SubAnime, WatchlistAnime>> {
        val nestedUpcoming = nestedUpcoming(schedule)
        return filterPairSpread(nestedUpcoming, watchlistAnimes)
    }

    private fun nestedUpcoming(schedule: Schedule): ArrayList<ArrayList<SubAnime>> {
        val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        val result: ArrayList<ArrayList<SubAnime>> = arrayListOf()
        for (x in 0..6) {
            val weekday = addWeekday(today, x)
            result.add(schedule.nthWeekday(weekday))
        }
        return result
    }

    private fun filterPairSpread (nestedUpcoming: ArrayList<ArrayList<SubAnime>>, watchlistAnimes: List<WatchlistAnime>): ArrayList<Pair<SubAnime, WatchlistAnime>> {
        val result: ArrayList<Pair<SubAnime, WatchlistAnime>> = arrayListOf()
        for (upcomingDay in nestedUpcoming) {
            upcomingDay.mapNotNull {
                val watchlistAnime = watchlistAnimes.firstOrNull { wa -> wa.id == it.mal_id }
                if (watchlistAnime == null) {
                    null
                } else {
                    Pair(it, watchlistAnime)
                }
            }
        }
        return result
    }
}
