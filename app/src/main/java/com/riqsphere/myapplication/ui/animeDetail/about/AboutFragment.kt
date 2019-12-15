package com.riqsphere.myapplication.ui.animeDetail.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime
import com.riqsphere.myapplication.R

class AboutFragment : Fragment(){

    private lateinit var synopsis: TextView
    private lateinit var aired: TextView
    private var firstRun = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_about,container,false)

        //setting content
        synopsis = view.findViewById(R.id.synopsis)
        aired = view.findViewById(R.id.aired)

        //feed
        if (firstRun) {
            synopsis.text = "Loading..."
            firstRun = false
        }

        return view
    }

    fun setAnime(anime: Anime) {
        synopsis.text = anime.synopsis
        aired.text = getAired(anime)
    }

    private fun getAired(anime: Anime): String {
        val aired = (anime.aired?.let { anime.aired.string }) ?: "Loading..."
        var processedAired: String
        var resultAppend = ""

        //treatment
        if (aired.length >= 15) {

        resultAppend += aired.substring(0, 3) + ", " //get the month letter
        processedAired = aired.substringAfter(", ") //ignore the day plus comma

        resultAppend += processedAired.substring(0, 4) + " - " //get the year

        processedAired = processedAired.substringAfter("to ") //ignore o "to "

        if (processedAired.length > 2) { //checks if the next element is a "? "
            resultAppend += processedAired.substring(0, 3)+", "
            processedAired = processedAired.substringAfter(", ")

            resultAppend += processedAired.substring(0, 4)
        } else {
            resultAppend += "Present"
        }
        }else {

            resultAppend = aired
        }

        return resultAppend
    }
}