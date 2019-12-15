package com.riqsphere.myapplication.ui.animeDetail.recs

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.model.watchlist.WatchlistAnime
import com.riqsphere.myapplication.room.MyaaViewModel
import com.riqsphere.myapplication.ui.onClickListeners.OpenAnimeDetail
import com.riqsphere.myapplication.ui.onClickListeners.WatchlistAdder
import com.riqsphere.myapplication.utils.ImageHandler

class RecsAdapter(private val activity: Activity, private val myaaViewModel: MyaaViewModel) : RecyclerView.Adapter<RecsAdapter.ViewHolder>() {

    private var recList = ArrayList<RecsModel>()
    private var watchlist: List<WatchlistAnime> = arrayListOf()

    fun setData(list: ArrayList<RecsModel>){
        this.recList = list
        notifyDataSetChanged()
    }

    fun setWatchlistData(list: List<WatchlistAnime>) {
        watchlist = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recs_card,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return recList.size
    }

    override fun onBindViewHolder(holder: RecsAdapter.ViewHolder, position: Int) {
        holder.bindView(position)
    }

    inner class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {

        private var recsImage:ImageView = itemView.findViewById(R.id.rec_image)
        private var recsTitle:TextView = itemView.findViewById(R.id.rec_anime_title)
        private var recsCount:TextView = itemView.findViewById(R.id.rec_score)
        private var recsAdded:ImageButton = itemView.findViewById(R.id.rec_add_to_list)

        fun bindView(position:Int){
            val item = recList[position]
            ImageHandler.getInstance(activity).load(item.urlImage).into(recsImage)
            recsImage.contentDescription = item.mal_id.toString()
            recsCount.text = item.animeRecsNum
            recsTitle.text = item.animeTitle

            val added = watchlist.any { it.id == item.mal_id }
            val resource = if (added) {
                R.drawable.ic_added_to_list
            } else {
                R.drawable.ic_add_to_list
            }
            recsAdded.setImageResource(resource)
            recsAdded.setOnClickListener(WatchlistAdder(myaaViewModel, item.mal_id, item.animeTitle, added))

            itemView.setOnClickListener(OpenAnimeDetail(activity, item.mal_id))
        }

    }

}