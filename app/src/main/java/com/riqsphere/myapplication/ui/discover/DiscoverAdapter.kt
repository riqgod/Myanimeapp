package com.riqsphere.myapplication.ui.discover

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.model.search.SearchModel
import com.riqsphere.myapplication.utils.ImageHandler

class DiscoverAdapter (private val mContext:Context) : RecyclerView.Adapter<DiscoverAdapter.ViewHolder>(){

    private lateinit var list:ArrayList<SearchModel>;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscoverAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.discover_card,parent,false)
        return ViewHolder(view)
    }

    fun setData(list:ArrayList<SearchModel>){
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DiscoverAdapter.ViewHolder, position: Int) {
        holder.bindView(position)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        private val cardAnimeImage: RoundedImageView = itemView.findViewById(R.id.dc_image)
        private val cardAnimeTitle: TextView = itemView.findViewById(R.id.dc_anime_title)
        private val cardAnimeScore: TextView = itemView.findViewById(R.id.dc_score)
        private val cardAnimeAdded: ImageView = itemView.findViewById(R.id.dc_add_to_list)

        fun bindView(position:Int){
            val dcCard = list[position]
            ImageHandler.getInstance(this@DiscoverAdapter.mContext).load(dcCard.imageURL).into(cardAnimeImage)
            cardAnimeTitle.setText(dcCard.animeTitle)
            cardAnimeScore.setText(dcCard.score)
            if(dcCard.added){
                cardAnimeAdded.setImageResource(R.drawable.ic_added_to_list)
            }else{
                cardAnimeAdded.setImageResource(R.drawable.ic_add_to_list)
            }

        }

    }

}