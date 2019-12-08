package com.riqsphere.myapplication.ui.watchlist

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.utils.ImageHandler
import com.riqsphere.myapplication.model.watchlist.room.WatchlistAnime

class WatchlistAdapter (private val context: Context) : RecyclerView.Adapter<WatchlistAdapter.WatchlistAnimeViewHolder>() {
    private val inflater = LayoutInflater.from(context)
    private var watchlistAnimes = emptyList<WatchlistAnime>()

    inner class WatchlistAnimeViewHolder(itemView: View):
        RecyclerView.ViewHolder(itemView) {
        @SuppressLint("ResourceType")
       private val cardAnimeImage: ImageView = itemView.findViewById(R.id.wl_image)
        private val cardAnimeTitle :TextView = itemView.findViewById(R.id.wl_anime_title)
        private val cardAnimeProgress: ProgressBar = itemView.findViewById(R.id.wl_progress_bar)

        fun bindView(wlCard: WatchlistAnime){
            ImageHandler.getInstance(this@WatchlistAdapter.context).load(wlCard.imgURL).into(cardAnimeImage)
            cardAnimeTitle.text = wlCard.title
            cardAnimeProgress.max = wlCard.episodes
            cardAnimeProgress.progress = wlCard.episodesOut // just a placeholder.
        }

    }

    @SuppressLint("ResourceType")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchlistAnimeViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.watchlist_card,parent,false)
        return WatchlistAnimeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WatchlistAnimeViewHolder, position: Int) {
        val current = watchlistAnimes[position]
        //manipulate holder using values from current
        holder.bindView(current)
    }

    internal fun setWatchlistAnime(watchlistAnimes: List<WatchlistAnime>) {
        this.watchlistAnimes = watchlistAnimes
        notifyDataSetChanged()
    }

    override fun getItemCount() = watchlistAnimes.size
}