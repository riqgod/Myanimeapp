package com.riqsphere.myapplication.ui.search

import android.app.Activity
import android.content.Context
import android.media.Image
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.model.search.SearchModel
import com.riqsphere.myapplication.utils.ImageHandler

class SearchAdapter : BaseAdapter() {

    private lateinit var list:ArrayList<SearchModel>;
    private lateinit var act:Activity;

    fun setData(list: ArrayList<SearchModel>,act:Activity){
        this.list = list
        this.act = act
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
       val view = act.layoutInflater.inflate(R.layout.anime_listview,parent,false)
        val sm = list.get(position)

        val searchAnimeTitle:TextView = view.findViewById(R.id.lv_anime_title)
        val searchImage:ImageView = view.findViewById(R.id.lv_image)
        val searchScore:TextView = view.findViewById(R.id.lv_score)
        val searchAdded:ImageView = view.findViewById(R.id.lv_add_to_list)
        val searchScoreStar: ImageView = view.findViewById(R.id.lv_star)
        val searchScoreBg: ImageView = view.findViewById(R.id.lv_rectangle_score)

        searchAnimeTitle.setText(sm.animeTitle)
        ImageHandler.getInstance(this@SearchAdapter.act).load(sm.imageURL).into(searchImage)
        searchScore.setText(sm.score)
        if(sm.added){
            searchAdded.setImageResource(R.drawable.ic_added_to_list)
        }else{
            searchAdded.setImageResource(R.drawable.ic_add_to_list)
        }

        searchScoreStar.setImageResource(R.drawable.ic_star_1)
        searchScoreBg.setImageResource(R.drawable.rectangle_score)

        return view;
    }

    override fun getItem(position: Int): Any {
        return list.get(position)
    }

    override fun getItemId(position: Int): Long {
        return 0;
    }

    override fun getCount(): Int {
        return list.size
    }

}