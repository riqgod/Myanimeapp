package com.riqsphere.myapplication.model.recommendations

import androidx.room.Entity
import com.github.doomsdayrs.jikan4java.types.support.recommendations.Recommend

@Entity(
    tableName = "recommendation",
    primaryKeys = ["from_id", "to_id"]
)
data class Recommendation (
    val from_id: Int,
    val to_id: Int,
    val count: Int
) {
    constructor(sourceId: Int, recommend: Recommend): this(
        sourceId,
        recommend.mal_id,
        recommend.recommendation_count
    )

    companion object {
        fun arrayListFrom(sourceId: Int, other: ArrayList<Recommend>) = other.mapTo(arrayListOf(), { Recommendation(sourceId, it) })
    }
}