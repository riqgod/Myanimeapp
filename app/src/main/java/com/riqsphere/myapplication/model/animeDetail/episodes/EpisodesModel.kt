package com.riqsphere.myapplication.model.animeDetail.episodes

import com.github.doomsdayrs.jikan4java.types.main.anime.videos.Episode as VideosEpisode

class EpisodesModel (
    val num: Int,
    val title: String,
    val urlVideo: String?,
    val imageUrl: String
){
    constructor(vid: VideosEpisode, position: Int): this(
        num = position,
        title = vid.title,
        urlVideo = vid.url,
        imageUrl = vid.image_url
    )
}