package com.riqsphere.myapplication.ui.animeDetail.episodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime
import com.riqsphere.myapplication.R

class EpisodesFragment(anime:Anime) : Fragment(){

    private val animus: Anime = anime

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_episodes,container,false)
        return view
    }
}