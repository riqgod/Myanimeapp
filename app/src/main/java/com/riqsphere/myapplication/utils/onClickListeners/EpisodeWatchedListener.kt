package com.riqsphere.myapplication.utils.onClickListeners

import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.model.watchlist.WatchlistAnime
import com.riqsphere.myapplication.room.MyaaViewModel

class EpisodeWatchedListener(
    private val myaaViewModel: MyaaViewModel,
    private val watchlistAnime: WatchlistAnime?,
    private val episode: Int,
    private val watched: Boolean,
    private val confirm: Boolean = false
): View.OnClickListener {

    private lateinit var iv: ImageView

    override fun onClick(v: View?) {
        if (v == null || v !is ImageView) {
            return
        }

        if (watchlistAnime == null) {
            Toast.makeText(v.context, "This anime is not in your watchlist yet. Add it to your watchlist first!", Toast.LENGTH_LONG).show()
            return
        }

        iv = v
        if (watched) {
            unwatch()
        } else {
            watch()
        }
    }

    private fun watch() {
        setImageAsWatched()
        myaaViewModel.addEpisodeWatched(watchlistAnime!!, episode)
    }

    private fun unwatch() {
        setImageAsUnwatched()
        myaaViewModel.removeEpisodeWatched(watchlistAnime!!, episode)
    }

    private fun setImageAsUnwatched() = iv.setImageResource(R.drawable.ic_button_check_as_watched)
    private fun setImageAsWatched() = iv.setImageResource(R.drawable.ic_button_checked_as_watched)

}