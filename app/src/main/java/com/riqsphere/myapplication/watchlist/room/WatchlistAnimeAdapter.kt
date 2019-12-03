package com.riqsphere.myapplication.watchlist.room

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WatchlistAnimeAdapter internal constructor (context: Context) : RecyclerView.Adapter<WatchlistAnimeAdapter.WatchlistAnimeViewHolder>() {
    private val inflater = LayoutInflater.from(context)
    private var watchlistAnimes = emptyList<WatchlistAnime>()

    inner class WatchlistAnimeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        @SuppressLint("ResourceType")
        val wordItemView: TextView = itemView.findViewById(1)
    }

    @SuppressLint("ResourceType")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchlistAnimeViewHolder {
        val itemView = inflater.inflate(123, parent, false)
        return WatchlistAnimeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WatchlistAnimeViewHolder, position: Int) {
        val current = watchlistAnimes[position]
        //manipulate holder using values from current
    }

    internal fun setWatchlistAnime(watchlistAnimes: List<WatchlistAnime>) {
        this.watchlistAnimes = watchlistAnimes
        notifyDataSetChanged()
    }

    override fun getItemCount() = watchlistAnimes.size
}