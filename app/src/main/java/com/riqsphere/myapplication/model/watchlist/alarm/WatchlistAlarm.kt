package com.riqsphere.myapplication.model.watchlist.alarm

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.riqsphere.myapplication.model.watchlist.room.WatchlistViewModel
import java.util.*
import kotlin.random.Random

object WatchlistAlarm : BroadcastReceiver() {
    fun setAlarm(application: Application) {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, Random.Default.nextInt(RAND_MIN, RAND_MAX))
        }

        val alarmMgr = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(application, WatchlistAlarm::class.java).let {
            PendingIntent.getBroadcast(application, 0, it, 0)
        }
        alarmMgr.setInexactRepeating(
            AlarmManager.RTC,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmIntent
        )

        val watchlistViewModel = WatchlistViewModel(application)
        updateAllEpisodes(watchlistViewModel)
    }

    private fun updateAllEpisodes(watchlistViewModel: WatchlistViewModel) {
        watchlistViewModel.allWatchlistAnime.value.let {
            it?.forEach {
                val anime = it.toAnime().get()
                watchlistViewModel.updateEpisodesOut(anime)
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val application = context.let { it as Application }
        setAlarm(application)
    }

    private const val RAND_MIN = 1
    private const val RAND_MAX = 5
}