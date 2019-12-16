package com.riqsphere.myapplication.ui.animeDetail.episodes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.doomsdayrs.jikan4java.types.main.anime.videos.Video
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.model.animeDetail.episodes.EpisodesModel
import com.riqsphere.myapplication.room.MyaaViewModel

class EpisodesFragment(private val animeId: Int) : Fragment(){

    private var myaaViewModel: MyaaViewModel? = null

    private var epTotal: TextView? = null
    private var wnRecyclerView: RecyclerView? = null
    private var wnViewAdapter: EpisodesWatchNextAdapter? = null
    private var allViewAdapter: EpisodesAllAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_episodes,container,false)
        if (activity == null) {
            return view
        }

        myaaViewModel = MyaaViewModel(activity!!.application)

        val checkAll = view.findViewById<ImageButton>(R.id.episodes_all_check_as_watched)
        checkAll.visibility = View.GONE

        epTotal = view.findViewById(R.id.episodes_all_total_text)

        val wnViewManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        wnViewAdapter = EpisodesWatchNextAdapter(activity!!.applicationContext, myaaViewModel!!)
        wnRecyclerView = view.findViewById<RecyclerView>(R.id.episodes_watch_next_rv).apply {
            layoutManager = wnViewManager
            adapter = wnViewAdapter
        }


        //all episodes [to com sono]
        val allViewManager = LinearLayoutManager(activity)
        allViewAdapter = EpisodesAllAdapter(activity!!.applicationContext, myaaViewModel!!)
        view.findViewById<RecyclerView>(R.id.episodes_all_rv).apply {
            layoutManager= allViewManager
            adapter = allViewAdapter!!
        }

        observe()

        return view
    }

    override fun onDetach() {
        super.onDetach()
        myaaViewModel = null
        epTotal = null
        wnRecyclerView = null
        wnViewAdapter = null
        allViewAdapter = null
    }

    private fun observe() {
        myaaViewModel?.allWatchlistAnime?.observe(this, Observer { list ->
            val wa = list?.firstOrNull { wa -> wa.id == animeId }
            wa?.let {
                epTotal?.text = it.episodesWatched.size.toString() + "/" + it.episodes.toString()
                allViewAdapter?.setWatchlistAnime(wa)
                wnViewAdapter?.setWatchlistAnime(wa)
            }
        })
    }

    fun setEpisodes(video: Video) {
        val episodes = video.episodes.mapIndexedTo(arrayListOf()) { index, episode -> EpisodesModel(episode, index) }
        allViewAdapter?.setEpisodes(episodes)
        wnViewAdapter?.setEpisodes(episodes)
    }
}