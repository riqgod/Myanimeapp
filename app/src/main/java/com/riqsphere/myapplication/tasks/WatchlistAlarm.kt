package com.riqsphere.myapplication.tasks

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.riqsphere.myapplication.room.MyaaViewModel
import com.riqsphere.myapplication.utils.getEpisodesOut
import java.util.*
import kotlin.random.Random

object WatchlistAlarm : BroadcastReceiver() {
    fun setAlarm(application: Application) {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, Random.Default.nextInt(
                RAND_MIN,
                RAND_MAX
            ))
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
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val application = context.let { it as Application }

        val watchlistViewModel =
            MyaaViewModel(application)
        updateAllEpisodes(
            watchlistViewModel
        )
    }

    private fun updateAllEpisodes(myaaViewModel: MyaaViewModel) {
        myaaViewModel.allWatchlistAnime.value.let {
            it?.forEach { watchlistAnime ->
                val updatedEpisodesOut = watchlistAnime.getEpisodesOut().get()
                myaaViewModel.updateEpisodesOut(watchlistAnime.id, updatedEpisodesOut)
            }
        }
    }

    private const val RAND_MIN = 1
    private const val RAND_MAX = 5
}