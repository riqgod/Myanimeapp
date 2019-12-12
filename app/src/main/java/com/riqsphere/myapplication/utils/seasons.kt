package com.riqsphere.myapplication.utils

import com.github.doomsdayrs.jikan4java.enums.Season
import java.util.*

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

fun Calendar.jikanSeasonCode(): Int {
    return when {
        monthIn(winterMonths) -> 0
        monthIn(springMonths) -> 1
        monthIn(summerMonths) -> 2
        monthIn(fallMonths) -> 3
        else -> -1
    }
}