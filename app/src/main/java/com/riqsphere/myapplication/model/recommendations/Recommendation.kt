package com.riqsphere.myapplication.model.recommendations

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recommendation")
data class Recommendation (
    @PrimaryKey
    val id: Int,
    val count: Int
)