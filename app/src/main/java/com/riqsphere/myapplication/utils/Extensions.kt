package com.riqsphere.myapplication.utils

import com.github.doomsdayrs.jikan4java.enums.Season
import com.github.doomsdayrs.jikan4java.types.main.schedule.Schedule
import com.github.doomsdayrs.jikan4java.types.main.schedule.SubAnime
import java.util.*
import kotlin.collections.ArrayList

val winterMonths = arrayOf(Calendar.JANUARY, Calendar.FEBRUARY, Calendar.MARCH)
val springMonths = arrayOf(Calendar.APRIL, Calendar.MAY, Calendar.JUNE)
val summerMonths = arrayOf(Calendar.JULY, Calendar.AUGUST, Calendar.SEPTEMBER)
val fallMonths = arrayOf(Calendar.OCTOBER, Calendar.NOVEMBER, Calendar.DECEMBER)

fun Calendar.monthIn(monthArray: Array<Int>): Boolean {
    val currentMonth = get(Calendar.MONTH)
    return monthArray.contains(currentMonth)
}

fun Calendar.jikanSeason(): Season? {
    return when {
        monthIn(winterMonths) -> Season.WINTER
        monthIn(springMonths) -> Season.SPRING
        monthIn(summerMonths) -> Season.SUMMER
        monthIn(fallMonths) -> Season.FALL
        else -> null
    }
}

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