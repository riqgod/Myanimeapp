package com.riqsphere.myapplication.utils

import com.github.doomsdayrs.jikan4java.types.main.schedule.Schedule
import com.github.doomsdayrs.jikan4java.types.main.schedule.SubAnime
import java.util.*
import kotlin.collections.ArrayList

fun Schedule.nthWeekday(n: Int): ArrayList<SubAnime> {
    return when (n) {
        Calendar.SUNDAY -> sunday
        Calendar.MONDAY -> monday
        Calendar.TUESDAY -> tuesday
        Calendar.WEDNESDAY -> wednesday
        Calendar.THURSDAY -> thursday
        Calendar.FRIDAY -> friday
        Calendar.SATURDAY -> saturday
        0 -> others
        else -> unknown
    }
}

fun addWeekday(today: Int, plus: Int): Int {
    val todayAdjusted = today - 1
    val unboundedSum = todayAdjusted + plus
    val boundedSum = unboundedSum % 7
    return boundedSum + 1
}