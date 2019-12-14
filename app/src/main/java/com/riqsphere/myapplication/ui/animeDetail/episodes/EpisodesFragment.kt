package com.riqsphere.myapplication.ui.animeDetail.episodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.model.animeDetail.episodes.EpisodesModel
import com.riqsphere.myapplication.utils.MarginItemDecoration

class EpisodesFragment(anime:Anime) : Fragment(){

    private val animus: Anime = anime

    private lateinit var wn_recyclerView: RecyclerView
    private lateinit var wn_viewAdapter: EpisodesWatchNextAdapter
    private lateinit var wn_viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_episodes,container,false)
        val image_url = animus.imageURL
        val epTotal = view.findViewById<TextView>(R.id.episodes_all_total_text) //preencher dps
        epTotal.text = "0/"+animus.episodes


        //watchnext!!!
        val model = animus.getEpisodes().get()
        val listModel = ArrayList<EpisodesModel>(0)

        for (i in model.episodes){
            listModel.add(EpisodesModel(i,image_url,false))
        }

        val epWatchNextAdapter = EpisodesWatchNextAdapter(activity!!.applicationContext)
        epWatchNextAdapter.setData(listModel)

        wn_viewManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        wn_viewAdapter = epWatchNextAdapter
        wn_recyclerView = view.findViewById<RecyclerView>(R.id.episodes_watch_next_rv).apply {
            layoutManager = wn_viewManager
            adapter = wn_viewAdapter
            addItemDecoration(MarginItemDecoration(175))
        }


        //all episodes [to com sono]
        val all_viewManager = LinearLayoutManager(activity)
        val all_viewAdapter = EpisodesAllAdapter(activity!!.applicationContext)
        all_viewAdapter.setData(listModel)
        val all_recyclewView = view.findViewById<RecyclerView>(R.id.episodes_all_rv).apply {
            layoutManager= all_viewManager
            adapter = all_viewAdapter
        }







        return view
    }
}