package com.suyf.calendarlist.widget

data class MonthBean(
    val title: String,
    var isReport: Boolean,
    val dayList: MutableList<DayBean> = mutableListOf()
)

data class DayBean(
    val year: Int,
    val month: Int,
    val day: Int,
    val isToday: Boolean = false,
    var isAvailable: Boolean = true,
    var isSclete: Boolean = false
)