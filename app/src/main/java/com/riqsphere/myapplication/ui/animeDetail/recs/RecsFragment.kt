package com.riqsphere.myapplication.ui.animeDetail.recs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime
import com.github.doomsdayrs.jikan4java.types.main.anime.animePage.AnimePageAnime
import com.github.doomsdayrs.jikan4java.types.support.recommendations.Recommend
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.cache.JikanCacheHandler
import com.riqsphere.myapplication.model.search.SearchModel
import com.riqsphere.myapplication.ui.discover.search.SearchAdapter

class RecsFragment(anime: Anime) : Fragment(){

    private val animus:Anime = anime

    private lateinit var rv:RecyclerView
    private lateinit var adapter: RecsAdapter
    private lateinit var model:ArrayList<RecsModel>
    private lateinit var viewManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_recs,container,false)

        val recsList = JikanCacheHandler.getRecommendationPage(animus)
        model = insertList(recsList.recommends)
        adapter = RecsAdapter(activity!!)
        adapter.setData(model)
        viewManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv = view.findViewById<RecyclerView>(R.id.recs_rv).apply {
            layoutManager = viewManager
            adapter = adapter
        }

        return view
    }

    private fun insertList(result: ArrayList<Recommend>): ArrayList<RecsModel> {
        var model = ArrayList<RecsModel>(0)
        for (i in result) {
            model.add(RecsModel(i,false))
        }
        return model
    }
}