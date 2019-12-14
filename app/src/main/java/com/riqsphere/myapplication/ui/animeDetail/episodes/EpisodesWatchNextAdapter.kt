package com.riqsphere.myapplication.ui.animeDetail.episodes

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.model.animeDetail.episodes.EpisodesModel
import com.riqsphere.myapplication.utils.ImageHandler
import java.util.*
import kotlin.collections.ArrayList


class EpisodesWatchNextAdapter(private val mContext:Context) : RecyclerView.Adapter<EpisodesWatchNextAdapter.ViewHolder>(){
    private var epList = ArrayList<EpisodesModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.episodes_watch_next_card,parent,false)
        return ViewHolder(view)
    }

    fun setData(list:ArrayList<EpisodesModel>){
        epList = list
    }

    override fun getItemCount(): Int {
        return epList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(position)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val cardEpImage: RoundedImageView = itemView.findViewById(R.id.episode_image)
        private val cardEpTitle: TextView = itemView.findViewById(R.id.episode_name)
        private val cardEpNum: TextView = itemView.findViewById(R.id.episode_num)
        private val cardEpChecked: ImageView = itemView.findViewById(R.id.episode_check_as_watched)

        fun bindView(position:Int){
            val epCard = epList[position]

            /*
            if(epCard.urlVideo == null){
                if(epCard.urlVideo != ""){
                    val frame = retriveVideoFrameFromVideo(epCard.urlVideo)
                    cardEpImage.setImageBitmap(frame)
                }else{
                    ImageHandler.getInstance(mContext).load(epCard.imageUrl).into(cardEpImage)
                }
            }else{

             */
                ImageHandler.getInstance(mContext).load(epCard.imageUrl).into(cardEpImage)
          //  }

            cardEpTitle.text = epCard.title
            cardEpNum.text = epCard.num
            if(epCard.watched){
                cardEpChecked.setImageResource(R.drawable.ic_button_checked_as_watched)
            }else{
               cardEpChecked.setImageResource(R.drawable.ic_button_check_as_watched)
            }
        }
    }

    @Throws(Throwable::class)
    fun retriveVideoFrameFromVideo(videoPath: String?): Bitmap? {
        var bitmap: Bitmap? = null
        var mediaMetadataRetriever: MediaMetadataRetriever? = null
        try {
            mediaMetadataRetriever = MediaMetadataRetriever()
            if (Build.VERSION.SDK_INT >= 14) mediaMetadataRetriever.setDataSource(
                videoPath,
                HashMap()
            ) else mediaMetadataRetriever.setDataSource(videoPath)
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.frameAtTime
        } catch (e: Exception) {
            e.printStackTrace()
            throw Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.message)
        } finally {
            mediaMetadataRetriever?.release()
        }
        return bitmap
    }

}