package com.riqsphere.myapplication.ui.main

import android.content.Context
import android.graphics.ColorSpace
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.doomsdayrs.jikan4java.core.Connector
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime

import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.watchlist.room.WatchlistAnime
import com.riqsphere.myapplication.watchlist.room.WatchlistAnimeAdapter
import com.riqsphere.myapplication.watchlist.room.WatchlistViewModel


class WatchlistFragment : Fragment() {

    private lateinit var recyclerView:RecyclerView
    private lateinit var viewAdapter: WatchlistAnimeAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var watchlistViewModel: WatchlistViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        watchlistViewModel = WatchlistViewModel(this.activity!!.application)
        val temporaryAnime = WatchlistAnime(temporaryFetch(20))
        watchlistViewModel.insert(temporaryAnime) //naruto

        val view = inflater.inflate(R.layout.fragment_watchlist,container,false)
        val watchlistAdapter = WatchlistAnimeAdapter(activity!!.applicationContext)


        viewManager = GridLayoutManager(activity,3)
        viewAdapter = watchlistAdapter
        recyclerView = view.findViewById<RecyclerView>(R.id.wl_rv).apply {
            layoutManager = viewManager
            setHasFixedSize(true)
            adapter = viewAdapter
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }

        return view
    }


    private fun temporaryFetch(id:Int):Anime{
        val anime = Connector().retrieveAnime(id).get()
        return anime
    }

}
