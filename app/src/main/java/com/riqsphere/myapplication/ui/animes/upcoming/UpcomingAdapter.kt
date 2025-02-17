package com.riqsphere.myapplication.ui.animes.upcoming

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.model.watchlist.WatchlistAnime
import com.riqsphere.myapplication.utils.ImageHandler
import com.riqsphere.myapplication.ui.onClickListeners.OpenAnimeDetail
import java.util.*

class UpcomingAdapter(private val activity: Activity): RecyclerView.Adapter<UpcomingAdapter.ViewHolder>() {
    private var upcoming = ArrayList<WatchlistAnime>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.upcoming_card, parent, false)
        return ViewHolder(view)
    }

    fun setData(upcoming: ArrayList<WatchlistAnime>) {
        this.upcoming = upcoming
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindView(position)

    override fun getItemCount() = upcoming.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val cardAnimeImage: RoundedImageView = itemView.findViewById(R.id.uc_image)
        private val cardAnimeTitle: TextView = itemView.findViewById(R.id.uc_anime_title)
        private val cardEpTitle: TextView = itemView.findViewById(R.id.uc_ep_title)
        private val cardEpNum: TextView = itemView.findViewById(R.id.uc_ep_num)
        private val cardTime: TextView = itemView.findViewById(R.id.uc_time)

        fun bindView(position: Int) {
            val up = upcoming[position]
            if (ImageHandler.shouldLoad()) {
                cardAnimeImage.contentDescription = "A image of the anime "+up.title
                ImageHandler.getInstance(this@UpcomingAdapter.activity).load(up.imgURL).into(cardAnimeImage)
            } else {
                ImageHandler.getInstance(this@UpcomingAdapter.activity).load(R.drawable.neko2).placeholder(R.drawable.neko2).into(cardAnimeImage)
                cardAnimeImage.contentDescription = "a car placeholder of anime loading image"
            }
            cardAnimeTitle.text = up.title
            cardEpNum.text = "Episode "+(up.episodesOut + 1).toString()
            cardTime.text = up.broadcast

            itemView.setOnClickListener(OpenAnimeDetail(this@UpcomingAdapter.activity, up.id))
        }
    }
}