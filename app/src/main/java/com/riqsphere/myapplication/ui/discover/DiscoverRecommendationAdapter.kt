package com.riqsphere.myapplication.ui.discover

import android.content.Context
import android.os.AsyncTask
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime
import com.makeramen.roundedimageview.RoundedImageView
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.cache.JikanCacheHandler
import com.riqsphere.myapplication.model.recommendations.Recommendation
import com.riqsphere.myapplication.model.watchlist.WatchlistAnime
import com.riqsphere.myapplication.utils.ImageHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class DiscoverRecommendationAdapter (private val mContext:Context) : RecyclerView.Adapter<DiscoverRecommendationAdapter.ViewHolder>(){
    private val mapIdIsInWatchlist = SparseBooleanArray()
    private var recList: List<Recommendation> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.discover_card,parent,false)
        return ViewHolder(view)
    }

    fun setWatchlistData(list: List<WatchlistAnime>) {
        squashWatchlist(list)
        notifyDataSetChanged()
    }

    private fun squashWatchlist(list: List<WatchlistAnime>) {
        mapIdIsInWatchlist.clear()
        list.forEach {
            mapIdIsInWatchlist.put(it.id, true)
        }
    }

    fun setRecListData(list: List<Recommendation>) {
        recList = list.filter { !mapIdIsInWatchlist[it.id] }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return recList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setLoading()
        holder.preBind(recList[position])
        BindAsync(holder, recList[position].id).execute()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val cardAnimeImage: RoundedImageView = itemView.findViewById(R.id.dc_image)
        private val cardAnimeTitle: TextView = itemView.findViewById(R.id.dc_anime_title)
        private val cardAnimeScore: TextView = itemView.findViewById(R.id.dc_score)
        private val cardAnimeAdded: ImageView = itemView.findViewById(R.id.dc_add_to_list)

        fun preBind(recommendation: Recommendation) {
            ImageHandler.getInstance(this@DiscoverRecommendationAdapter.mContext).load(recommendation.imageURL).into(cardAnimeImage)
            cardAnimeImage.contentDescription = recommendation.id.toString()
            cardAnimeTitle.text = recommendation.title
            cardAnimeAdded.setImageResource(R.drawable.ic_add_to_list)
        }

        fun bindView(anime: Anime) {
            cardAnimeScore.text = anime.score.toString()
        }

        fun setLoading() {
            ImageHandler.getInstance(this@DiscoverRecommendationAdapter.mContext).load(R.drawable.neko).into(cardAnimeImage)
            cardAnimeTitle.text = "Loading..."
            cardAnimeScore.text = ""
        }
    }

    private class BindAsync(private val holder: ViewHolder, private val animeId: Int): AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? = runBlocking {
            val anime = withContext(Dispatchers.Unconfined) {
                JikanCacheHandler.getAnime(animeId)
            }
            withContext(Dispatchers.Main) {
                holder.bindView(anime)
            }
            null
        }
    }
}