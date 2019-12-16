package com.riqsphere.myapplication.utils.onClickListeners

import android.app.Activity
import android.content.Intent
import android.view.View
import com.riqsphere.myapplication.ui.animeDetail.AnimeDetailActivity

class OpenAnimeDetail(
    private val activity: Activity,
    private val id: Int
): View.OnClickListener {
    override fun onClick(v: View?) {
        val i = Intent(activity, AnimeDetailActivity::class.java)
        i.putExtra("id", id)
        activity.startActivity(i)
    }
}