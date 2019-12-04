package com.riqsphere.myapplication.ui.main

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.upcoming.UpcomingAdapter
import com.riqsphere.myapplication.upcoming.UpcomingModel

class UpcomingFragment : Fragment() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


       val view = inflater.inflate(R.layout.fragment_upcoming, container, false)
        val upcomingCardList = ArrayList<UpcomingModel>()

        upcomingCardList.add(UpcomingModel(R.drawable.a102113,"Boku no Hero Academia...","S04 | EP01", "An Unpleaseant Talk","10:00 am"))
        upcomingCardList.add(UpcomingModel(R.drawable.a102113,"Boku no Hero Academia...","S04 | EP01", "An Unpleaseant Talk","10:00 am"))
        upcomingCardList.add(UpcomingModel(R.drawable.a102113,"Boku no Hero Academia...","S04 | EP01", "An Unpleaseant Talk","10:00 am"))
        upcomingCardList.add(UpcomingModel(R.drawable.a102113,"Boku no Hero Academia...","S04 | EP01", "An Unpleaseant Talk","10:00 am"))
        upcomingCardList.add(UpcomingModel(R.drawable.a102113,"Boku no Hero Academia...","S04 | EP01", "An Unpleaseant Talk","10:00 am"))
        upcomingCardList.add(UpcomingModel(R.drawable.a102113,"Boku no Hero Academia...","S04 | EP01", "An Unpleaseant Talk","10:00 am"))

        val upcomingAdapter:UpcomingAdapter = UpcomingAdapter(activity,upcomingCardList)

        viewManager = LinearLayoutManager(activity)
        viewAdapter = upcomingAdapter
        recyclerView = view.findViewById<RecyclerView>(R.id.uc_rv).apply {
            layoutManager = viewManager
            setHasFixedSize(true)
            adapter = viewAdapter
            addItemDecoration(DividerItemDecoration(activity,DividerItemDecoration.VERTICAL))
        }

        return view;
    }

}
