package com.riqsphere.myapplication.ui.main

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.watchlist.room.WatchlistViewModel


class WatchlistFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d("debugg","watchhhh")
        return inflater.inflate(R.layout.fragment_watchlist, container, false)
    }
}
