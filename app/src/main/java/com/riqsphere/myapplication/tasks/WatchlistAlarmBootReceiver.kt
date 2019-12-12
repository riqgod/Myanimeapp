package com.riqsphere.myapplication.tasks

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class WatchlistAlarmBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ACTION_BOOT_COMPLETED) {
            val application = context?.applicationContext as Application
            WatchlistAlarm.setAlarm(application)
        }
    }

    companion object {
        private const val ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED"
    }
}