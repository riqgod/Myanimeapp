package com.riqsphere.myapplication.ui.animeDetail.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.ui.animeDetail.AnimeDetailActivity

class AboutFragment(anime: Anime) : Fragment(){

    private val animus:Anime = anime

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_about,container,false)

        //setting content
        val synopsis = view.findViewById<TextView>(R.id.synopsis)
        val aired = view.findViewById<TextView>(R.id.aired)

        //feed
        synopsis.setText(animus.synopsis)
        aired.setText(getAired(animus))

        return view
    }

    fun getAired(anime:Anime): String {
        val aired = anime.aired.string
        var processedAired: String = ""
        var resultAppend: String = ""

        //treatment
        if (aired.length >= 15) {

        resultAppend += aired.substring(0, 3) + ", " //get the month letter
        processedAired = aired.substringAfter(", ") //ignore the day plus comma

        resultAppend += processedAired.substring(0, 4) + " - " //get the year

        processedAired = processedAired.substringAfter("to ") //ignore o "to "

        if (processedAired.length > 2) { //checks if the next element is a "? "
            resultAppend += processedAired.substring(0, 3)
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