package com.riqsphere.myapplication.ui.animes.upcoming

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.model.upcoming.UpcomingModel
import com.riqsphere.myapplication.utils.ImageHandler
import java.util.*

class UpcomingAdapter(private val mContext: Context): RecyclerView.Adapter<UpcomingAdapter.ViewHolder>() {
    private var upcoming = ArrayList<UpcomingModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.upcoming_card, parent, false)
        return ViewHolder(view)
    }

    fun setData(upcoming: ArrayList<UpcomingModel>) {
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
            val ucCard = upcoming[position]
            ImageHandler.getInstance(this@UpcomingAdapter.mContext).load(ucCard.imageURL).into(cardAnimeImage)
            cardAnimeTitle.text = ucCard.animeTitle
            cardEpNum.text = ucCard.epNum
            cardTime.text = ucCard.time
        }
    }
}