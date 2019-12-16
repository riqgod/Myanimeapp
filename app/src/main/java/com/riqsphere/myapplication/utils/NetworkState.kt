package com.riqsphere.myapplication.utils

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

object NetworkState {
    private lateinit var cm: ConnectivityManager
    private lateinit var p: SharedPreferences

    fun init(activity: AppCompatActivity) {
        cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        p = PreferenceManager(activity).sharedPreferences
    }

    fun shouldLoad(): Boolean {
        val mobileDataAllowed = p.getBoolean("images_over_mobile", true)
        return if (mobileDataAllowed) {
            true
        } else {
            wifi()
        }
    }

    private fun wifi() = cm.activeNetwork?.let {
        cm.getNetworkCapabilities(it)?.hasCapability(NetworkCapabilities.NET_CAPABILITY_WIFI_P2P) ?: false
    } ?: false
}