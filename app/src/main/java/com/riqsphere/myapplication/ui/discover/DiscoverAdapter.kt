package com.riqsphere.myapplication.ui.discover

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.model.search.SearchModel
import com.riqsphere.myapplication.model.watchlist.WatchlistAnime
import com.riqsphere.myapplication.room.MyaaViewModel
import com.riqsphere.myapplication.ui.onClickListeners.OpenAnimeDetail
import com.riqsphere.myapplication.ui.onClickListeners.WatchlistAdder
import com.riqsphere.myapplication.utils.ImageHandler

class DiscoverAdapter(private val activity: Activity, private val myaaViewModel: MyaaViewModel) : RecyclerView.Adapter<DiscoverAdapter.ViewHolder>(){

    private var searchList = ArrayList<SearchModel>()
    private var watchlist: List<WatchlistAnime> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.discover_card,parent,false)
        return ViewHolder(view)
    }

    fun setSearchData(list: ArrayList<SearchModel>) {
        searchList = list
        notifyDataSetChanged()
    }

    fun setWatchlistData(list: List<WatchlistAnime>) {
        watchlist = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setLoading()
        holder.bindView(position)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val cardAnimeImage: RoundedImageView = itemView.findViewById(R.id.dc_image)
        private val cardAnimeTitle: TextView = itemView.findViewById(R.id.dc_anime_title)
        private val cardAnimeScore: TextView = itemView.findViewById(R.id.dc_score)
        private val cardAnimeAdded: ImageView = itemView.findViewById(R.id.dc_add_to_list)

        fun bindView(position:Int) {
            val dcCard = searchList[position]
            if (dcCard.imageURL != "") {
                ImageHandler.getInstance(this@DiscoverAdapter.activity).load(dcCard.imageURL).into(cardAnimeImage)
            }
            cardAnimeImage.contentDescription = dcCard.id.toString()
            cardAnimeTitle.text = dcCard.animeTitle
            cardAnimeScore.text = dcCard.score

            if (watchlist.any { it.id == dcCard.id }) {
                cardAnimeAdded.setImageResource(R.drawable.ic_added_to_list)
                cardAnimeAdded.setOnClickListener(WatchlistAdder(myaaViewModel, dcCard.id, dcCard.animeTitle, true))
            } else {
                cardAnimeAdded.setImageResource(R.drawable.ic_add_to_list)
                cardAnimeAdded.setOnClickListener(WatchlistAdder(myaaViewModel, dcCard.id, dcCard.animeTitle, false))
            }

            itemView.setOnClickListener(OpenAnimeDetail(this@DiscoverAdapter.activity, dcCard.id))
        }

        fun setLoading() {
            ImageHandler.getInstance(this@DiscoverAdapter.activity).load(R.drawable.neko).into(cardAnimeImage)
            cardAnimeTitle.text = "Loading..."
            cardAnimeScore.text = ""
        }
    }
}