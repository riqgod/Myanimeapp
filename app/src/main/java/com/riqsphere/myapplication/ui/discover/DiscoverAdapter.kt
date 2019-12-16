package com.riqsphere.myapplication.ui.discover

import android.app.Activity
import android.util.SparseBooleanArray
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
import com.riqsphere.myapplication.utils.ImageHandler
import com.riqsphere.myapplication.ui.onClickListeners.OpenAnimeDetail
import com.riqsphere.myapplication.ui.onClickListeners.WatchlistAdder

class DiscoverAdapter(private val activity: Activity, private val myaaViewModel: MyaaViewModel) : RecyclerView.Adapter<DiscoverAdapter.ViewHolder>(){

    private var searchList = ArrayList<SearchModel>()
    private var watchlist = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.discover_card,parent,false)
        return ViewHolder(view)
    }

    fun setSearchData(list: ArrayList<SearchModel>) {
        searchList = list
        notifyDataSetChanged()
    }

    fun setWatchlistData(list: List<WatchlistAnime>) {
        watchlist.clear()
        list.forEach { watchlist.put(it.id, true) }
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
            if (dcCard.imageURL != "" && ImageHandler.shouldLoad()) {
                cardAnimeImage.contentDescription = "A image of the anime "+dcCard.animeTitle
                ImageHandler.getInstance(this@DiscoverAdapter.activity).load(dcCard.imageURL).into(cardAnimeImage)
            } else {
                ImageHandler.getInstance(this@DiscoverAdapter.activity).load(R.drawable.neko).placeholder(R.drawable.neko).into(cardAnimeImage)
                cardAnimeImage.contentDescription = "A cat in the placeholder to a loading anime image"
            }
            cardAnimeImage.contentDescription = dcCard.id.toString()
            cardAnimeTitle.text = dcCard.animeTitle
            cardAnimeScore.text = dcCard.score
            cardAnimeScore.contentDescription = "the score of "+dcCard.animeTitle+" if "+dcCard.score

            if (watchlist[dcCard.id]) {
                cardAnimeAdded.setImageResource(R.drawable.ic_added_to_list)
                cardAnimeAdded.contentDescription = dcCard.animeTitle + " is in your watchlist"
                //cardAnimeAdded.setOnClickListener(WatchlistAdder(myaaViewModel, dcCard.id, dcCard.animeTitle, true))
            } else {
                cardAnimeAdded.visibility = View.GONE
                //cardAnimeAdded.setImageResource(R.drawable.ic_add_to_list)
                //cardAnimeAdded.contentDescription = "add " + dcCard.animeTitle + " to your watchlist"
                //cardAnimeAdded.setOnClickListener(WatchlistAdder(myaaViewModel, dcCard.id, dcCard.animeTitle, false))
            }

            itemView.setOnClickListener(OpenAnimeDetail(this@DiscoverAdapter.activity, dcCard.id))
        }

        fun setLoading() {
            ImageHandler.getInstance(this@DiscoverAdapter.activity).load(R.drawable.neko).placeholder(R.drawable.neko).into(cardAnimeImage)
            cardAnimeTitle.text = "Loading..."
            cardAnimeScore.text = ""
        }
    }
}