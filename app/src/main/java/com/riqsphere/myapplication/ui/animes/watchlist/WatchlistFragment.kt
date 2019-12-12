package com.riqsphere.myapplication.ui.animes.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.cache.JikanCacheHandler
import com.riqsphere.myapplication.model.watchlist.WatchlistAnime
import com.riqsphere.myapplication.room.MyaaViewModel


class WatchlistFragment : Fragment() {

    private lateinit var recyclerView:RecyclerView
    private lateinit var viewAdapter: WatchlistAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var myaaViewModel: MyaaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myaaViewModel =
            MyaaViewModel(this.activity!!.application)
        myaaViewModel.allWatchlistAnime.observe(this, Observer {
            viewAdapter.setData(it)
        })

        val naruto = WatchlistAnime(
            temporaryFetch(20)
        )
        myaaViewModel.insert(naruto) //naruto
        val hero4 = WatchlistAnime(
            temporaryFetch(38408)
        )
        myaaViewModel.insert(hero4) //boku no hero academia 4
        val nrt = WatchlistAnime(
            temporaryFetch(20)
        )
        myaaViewModel.insert(nrt) //naruto
        val random = WatchlistAnime(
            temporaryFetch(21)
        )
        myaaViewModel.insert(random) //random id
        val rdm = WatchlistAnime(
            temporaryFetch(22)
        )
        myaaViewModel.insert(rdm)

        val view = inflater.inflate(R.layout.fragment_watchlist,container,false)
        val watchlistAdapter = WatchlistAdapter(activity!!.applicationContext)

        viewManager = GridLayoutManager(activity,3)
        viewAdapter = watchlistAdapter
        recyclerView = view.findViewById<RecyclerView>(R.id.wl_rv).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        return view
    }


    private fun temporaryFetch(id: Int): Anime{
        return JikanCacheHandler.getAnime(id)
    }

}
