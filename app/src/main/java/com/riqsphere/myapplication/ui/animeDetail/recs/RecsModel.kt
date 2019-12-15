package com.riqsphere.myapplication.ui.animeDetail.recs

import com.github.doomsdayrs.jikan4java.types.support.recommendations.Recommend

class RecsModel (
    val animeTitle:String,
    val animeRecsNum:String,
    val animeAddedToWatchList:Boolean,
    val urlImage:String,
    val mal_id:Int
){
    constructor(rec: Recommend, added:Boolean):this(
        animeAddedToWatchList = added,
        animeRecsNum = rec.recommendation_count.toString(),
        animeTitle = rec.title,
        urlImage = rec.image_url,
        mal_id = rec.mal_id
    )

}