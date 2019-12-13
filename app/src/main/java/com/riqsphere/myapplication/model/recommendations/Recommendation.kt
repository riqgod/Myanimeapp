package com.riqsphere.myapplication.model.recommendations

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.doomsdayrs.jikan4java.types.support.recommendations.Recommend

@Entity(
    tableName = "recommendation"
)
data class Recommendation (
    @PrimaryKey
    val id: Int,
    var count: Int,
    val imageURL: String,
    val title: String
) {
    constructor(recommend: Recommend): this(
        recommend.mal_id,
        recommend.recommendation_count,
        recommend.image_url,
        recommend.title
    )

    companion object {
        fun arrayListFrom(other: ArrayList<Recommend>) = other.mapTo(arrayListOf(), { Recommendation(it) })
    }
}