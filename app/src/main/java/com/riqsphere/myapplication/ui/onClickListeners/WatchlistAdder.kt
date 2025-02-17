package com.riqsphere.myapplication.ui.onClickListeners

import android.os.AsyncTask
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.cache.JikanCacheHandler
import com.riqsphere.myapplication.room.MyaaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class WatchlistAdder(
    private val myaaViewModel: MyaaViewModel,
    private val id: Int,
    private val title: String,
    private val added: Boolean
): View.OnClickListener {
    override fun onClick(v: View?) {
        if (v == null) {
            return
        }

        if (added) {
            AlertDialog.Builder(v.context)
                .setTitle("Remove anime from watchlist")
                .setMessage("Are you sure you want to remove $title from your watchlist?")
                .setIcon(R.drawable.neko)
                .setPositiveButton("Yes") { _, _ ->
                    myaaViewModel.delete(id)
                    if (v is ImageView) {
                        v.setImageResource(R.drawable.ic_add_to_list)
                    }
                }
                .setNegativeButton("No", null)
                .show()
        } else {
            AlertDialog.Builder(v.context)
                .setTitle("Add anime to watchlist")
                .setMessage("Are you sure you want to add $title to your watchlist?")
                .setIcon(R.drawable.neko)
                .setPositiveButton("Yes") { _, _ ->
                    AsyncAddAnime(myaaViewModel, id).execute()
                    if (v is ImageView) {
                        v.setImageResource(R.drawable.ic_added_to_list)
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    private class AsyncAddAnime(
        private val myaaViewModel: MyaaViewModel,
        private val id: Int
    ): AsyncTask<Void, Void, Void> () {
        override fun doInBackground(vararg params: Void?): Void? = runBlocking {
            val anime = withContext(Dispatchers.Unconfined) {
                JikanCacheHandler.getAnime(id)
            }
            if (anime.title != JikanCacheHandler.INTERNET_UNAVAILABLE) {
                myaaViewModel.insert(anime)
            }
            null
        }
    }
}