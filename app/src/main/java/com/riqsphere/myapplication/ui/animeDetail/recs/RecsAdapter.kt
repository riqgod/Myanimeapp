package com.riqsphere.myapplication.ui.animeDetail.recs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.utils.ImageHandler

class RecsAdapter(private val context:Context) : RecyclerView.Adapter<RecsAdapter.ViewHolder>() {

    private var list = ArrayList<RecsModel>()

    fun setData(list: ArrayList<RecsModel>){
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recs_card,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecsAdapter.ViewHolder, position: Int) {
        holder.bindView(position)
    }

    inner class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {

        private var recsImage:ImageView = itemView.findViewById(R.id.rec_image)
        private var recsTitle:TextView = itemView.findViewById(R.id.rec_anime_title)
        private var recsCount:TextView = itemView.findViewById(R.id.rec_score)
        private var recsAdded:ImageButton = itemView.findViewById(R.id.rec_add_to_list)

        fun bindView(position:Int){
            val item = list[position]
            ImageHandler.getInstance(context).load(item.urlImage).into(recsImage)
            recsCount.setText(item.animeRecsNum)
            recsTitle.setText(item.animeTitle)

            if(item.animeAddedToWatchList){
                recsAdded.setImageResource(R.drawable.ic_added_to_list)
            }else{
                recsAdded.setImageResource(R.drawable.ic_add_to_list)
            }

        }

    }

}