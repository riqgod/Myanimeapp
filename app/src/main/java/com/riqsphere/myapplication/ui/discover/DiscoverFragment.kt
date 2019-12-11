package com.riqsphere.myapplication.ui.animes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.cache.JikanCacheHandler
import com.riqsphere.myapplication.model.search.SearchModel
import com.riqsphere.myapplication.ui.discover.DiscoverAdapter


class DiscoverFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: DiscoverAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var discoverViewModel: ArrayList<SearchModel>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_discover, container, false)

        //temporary model
        discoverViewModel = ArrayList<SearchModel>(5)
        val anime1 = temporaryFetch(23);
        val anime2 = temporaryFetch(24);
        val anime3 = temporaryFetch(25);
        val anime4 = temporaryFetch(26);
        val anime5 = temporaryFetch(27);

        discoverViewModel.add(SearchModel(anime1,false))
        discoverViewModel.add(SearchModel(anime2,true))
        discoverViewModel.add(SearchModel(anime3,false))
        discoverViewModel.add(SearchModel(anime4,false))
        discoverViewModel.add(SearchModel(anime5,true))


        val discoverAdapter = DiscoverAdapter(activity!!.applicationContext)


        viewManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        viewAdapter = discoverAdapter
        viewAdapter.setData(discoverViewModel)

        //1
        recyclerView = view.findViewById<RecyclerView>(R.id.dc_rv_top4u).apply{
            layoutManager = viewManager
            adapter = viewAdapter
        }

        //2
        val viewManager2 = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        val rv2:RecyclerView = view.findViewById<RecyclerView>(R.id.dc_rv_mp).apply {
            layoutManager = viewManager2
            adapter = viewAdapter
        }

        //3
        val viewManager3 = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val rv3:RecyclerView = view.findViewById<RecyclerView>(R.id.dc_rv_score).apply {
            layoutManager = viewManager3
            adapter = viewAdapter
        }
        //4
        val viewManager4 = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val rv4:RecyclerView = view.findViewById<RecyclerView>(R.id.dc_rv_seasonal).apply {
            layoutManager = viewManager4
            adapter = viewAdapter
        }
        //5
        val viewManager5 = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val rv5:RecyclerView = view.findViewById<RecyclerView>(R.id.dc_rv_airing).apply {
            layoutManager = viewManager5
            adapter = viewAdapter
        }
        //6
        val viewManager6 = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val rv6:RecyclerView = view.findViewById<RecyclerView>(R.id.dc_rv_top_upcoming).apply {
            layoutManager = viewManager6
            adapter = viewAdapter
        }
        //7
        val viewManager7 = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val rv7:RecyclerView = view.findViewById<RecyclerView>(R.id.dc_rv_comedy).apply {
            layoutManager = viewManager7
            adapter = viewAdapter
        }


        return view
    }


    private fun temporaryFetch(id: Int): Anime {
        return JikanCacheHandler.getAnime(id)
    }
}