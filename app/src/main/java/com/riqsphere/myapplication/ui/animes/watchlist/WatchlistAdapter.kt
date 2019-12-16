package com.riqsphere.myapplication.ui.animes.watchlist

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.model.watchlist.WatchlistAnime
import com.riqsphere.myapplication.utils.ImageHandler
import com.riqsphere.myapplication.ui.onClickListeners.OpenAnimeDetail

class WatchlistAdapter(private val activity: Activity) : RecyclerView.Adapter<WatchlistAdapter.WatchlistAnimeViewHolder>() {
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
        private val cardAnimeImage: ImageButton = itemView.findViewById(R.id.wl_image)
        private val cardAnimeTitle :TextView = itemView.findViewById(R.id.wl_anime_title)
        private val cardAnimeProgress: ProgressBar = itemView.findViewById(R.id.wl_progress_bar)

        fun bindView(position: Int) {
            val watchlistAnime = watchlistAnimes[position]
            if (ImageHandler.shouldLoad()) {
                cardAnimeImage.contentDescription = "A image of the anime "+ watchlistAnime.title
                ImageHandler.getInstance(activity).load(watchlistAnime.imgURL).into(cardAnimeImage)
            } else {
                ImageHandler.getInstance(activity).load(R.drawable.neko2).placeholder(R.drawable.neko2).into(cardAnimeImage)
                cardAnimeImage.contentDescription = "A cat placeholder of anime loading image"
            }
            cardAnimeImage.contentDescription = watchlistAnime.id.toString()
            cardAnimeTitle.text = watchlistAnime.title
            cardAnimeProgress.max = watchlistAnime.episodes
            cardAnimeProgress.progress = watchlistAnime.episodesWatched.size // just a placeholder.

            cardAnimeImage.setOnClickListener(OpenAnimeDetail(activity, watchlistAnime.id))
        }
    }
}