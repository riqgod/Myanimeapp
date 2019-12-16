package com.riqsphere.myapplication.utils

import android.content.Context
import com.squareup.picasso.Cache
import com.squareup.picasso.Picasso
import java.util.concurrent.Executors

object ImageHandler {
    private var INSTANCE: Picasso? = null

    fun getInstance(context: Context): Picasso {
        val tempInstance = INSTANCE
        tempInstance?.let { return it }

        synchronized(this) {
            val instance = Picasso.Builder(context).executor(Executors.newSingleThreadExecutor()).indicatorsEnabled(true).memoryCache(Cache.NONE).build()
            INSTANCE = instance
            return instance
        }
    }
}