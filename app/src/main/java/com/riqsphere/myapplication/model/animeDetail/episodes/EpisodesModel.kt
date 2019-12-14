package com.riqsphere.myapplication.model.animeDetail.episodes

import com.github.doomsdayrs.jikan4java.types.main.anime.episodes.Episode

class EpisodesModel (
    val num:String,
    val title:String,
    val urlVideo:String?,
    var watched: Boolean,
    val imageUrl:String
){
    constructor(ep: Episode, imageUrl:String, watched:Boolean):this(
        num = "Episode "+ ep.episode_id.toString(),
        title = ep.title,
        urlVideo = ep.video_url,
        watched = watched,
        imageUrl = imageUrl


    )
}