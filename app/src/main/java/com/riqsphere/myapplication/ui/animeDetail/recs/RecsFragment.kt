package com.riqsphere.myapplication.ui.animeDetail.recs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime
import com.github.doomsdayrs.jikan4java.types.main.anime.animePage.AnimePageAnime
import com.github.doomsdayrs.jikan4java.types.support.recommendations.Recommend
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.cache.JikanCacheHandler
import com.riqsphere.myapplication.model.search.SearchModel
import com.riqsphere.myapplication.ui.discover.search.SearchAdapter

class RecsFragment(anime: Anime) : Fragment(){

    private val animus:Anime = anime

    private lateinit var list:ListView
    private lateinit var adapter: SearchAdapter
    private lateinit var model:ArrayList<SearchModel>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_recs,container,false)


        //generate sample data

        val recsList = JikanCacheHandler.getRecommendationPage(animus)
        model = insertList(recsList.recommends)


        //locate the listview in recs
        list = view.findViewById<ListView>(R.id.recs_listview)

        //pass results to ListViewAdapter
        adapter = SearchAdapter(activity!!)
        adapter.setData(model)

        //binds the adapter to the list view
        list.adapter = adapter



        return view
    }

    private fun insertList(result: ArrayList<Recommend>): ArrayList<SearchModel> {
        var model = ArrayList<SearchModel>(0)
        for (i in result) {
            model.add(SearchModel(i))
        }
        return model
    }
}