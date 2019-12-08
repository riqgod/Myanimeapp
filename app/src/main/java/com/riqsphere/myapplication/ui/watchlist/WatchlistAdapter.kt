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

    @SuppressLint("ResourceType")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchlistAnimeViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.watchlist_card, parent, false)
        return WatchlistAnimeViewHolder(itemView)
    }

    fun setData(watchlistAnimes: List<WatchlistAnime>) {
        this.watchlistAnimes = watchlistAnimes
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: WatchlistAnimeViewHolder, position: Int) = holder.bindView(position)

    override fun getItemCount() = watchlistAnimes.size

    inner class WatchlistAnimeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val cardAnimeImage: ImageView = itemView.findViewById(R.id.wl_image)
        private val cardAnimeTitle :TextView = itemView.findViewById(R.id.wl_anime_title)
        private val cardAnimeProgress: ProgressBar = itemView.findViewById(R.id.wl_progress_bar)

        fun bindView(position: Int) {
            val watchlistAnime = watchlistAnimes[position]
            ImageHandler.getInstance(this@WatchlistAdapter.context).load(watchlistAnime.imgURL).into(cardAnimeImage)
            cardAnimeTitle.text = watchlistAnime.title
            cardAnimeProgress.max = watchlistAnime.episodes
            cardAnimeProgress.progress = watchlistAnime.episodesOut // just a placeholder.
        }
    }
}