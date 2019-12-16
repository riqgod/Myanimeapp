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
import com.riqsphere.myapplication.model.recommendations.Recommendation
import com.riqsphere.myapplication.model.watchlist.WatchlistAnime
import com.riqsphere.myapplication.room.MyaaViewModel
import com.riqsphere.myapplication.utils.ImageHandler
import com.riqsphere.myapplication.utils.onClickListeners.OpenAnimeDetail
import com.riqsphere.myapplication.utils.onClickListeners.WatchlistAdder

class DiscoverRecommendationAdapter(private val activity: Activity, private val myaaViewModel: MyaaViewModel) : RecyclerView.Adapter<DiscoverRecommendationAdapter.ViewHolder>(){
    private val watchlist = SparseBooleanArray()
    private var originalRecList: List<Recommendation> = arrayListOf()
    private var recList: List<Recommendation> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.discover_card,parent,false)
        return ViewHolder(view)
    }

    fun setWatchlistData(list: List<WatchlistAnime>) {
        watchlist.clear()
        list.forEach { watchlist.put(it.id, true) }
        recList = filterRecsInWatchlist(originalRecList)
        notifyDataSetChanged()
    }

    fun setRecListData(list: List<Recommendation>) {
        originalRecList = list
        recList = filterRecsInWatchlist(list)
        notifyDataSetChanged()
    }

    private fun filterRecsInWatchlist(list: List<Recommendation>) = list.filter {
        !watchlist[it.id]
    }

    override fun getItemCount(): Int {
        return recList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setLoading()
        holder.preBind(recList[position])
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val cardAnimeImage: RoundedImageView = itemView.findViewById(R.id.dc_image)
        private val cardAnimeTitle: TextView = itemView.findViewById(R.id.dc_anime_title)
        private val cardAnimeScore: TextView = itemView.findViewById(R.id.dc_score)
        private val cardAnimeAdded: ImageView = itemView.findViewById(R.id.dc_add_to_list)

        fun preBind(recommendation: Recommendation) {
            if (ImageHandler.shouldLoad()) {
                ImageHandler.getInstance(this@DiscoverRecommendationAdapter.activity).load(recommendation.imageURL).placeholder(R.drawable.neko).into(cardAnimeImage)
            } else {
                ImageHandler.getInstance(this@DiscoverRecommendationAdapter.activity).load(R.drawable.neko).placeholder(R.drawable.neko).into(cardAnimeImage)
            }
            cardAnimeImage.contentDescription = recommendation.id.toString()
            cardAnimeTitle.text = recommendation.title
            cardAnimeScore.text = recommendation.count.toString()
            cardAnimeAdded.setImageResource(R.drawable.ic_add_to_list)
            cardAnimeAdded.setOnClickListener(WatchlistAdder(myaaViewModel, recommendation.id, recommendation.title, false))
            itemView.setOnClickListener(OpenAnimeDetail(this@DiscoverRecommendationAdapter.activity, recommendation.id))
        }

        fun setLoading() {
            ImageHandler.getInstance(this@DiscoverRecommendationAdapter.activity).load(R.drawable.neko).placeholder(R.drawable.neko).into(cardAnimeImage)
            cardAnimeTitle.text = "Loading..."
            cardAnimeScore.text = ""
        }
    }
}