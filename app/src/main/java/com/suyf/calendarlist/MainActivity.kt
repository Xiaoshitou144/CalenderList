package com.suyf.calendarlist

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.suyf.calendarlist.widget.DateUtils
import com.suyf.calendarlist.widget.DayBean
import com.suyf.calendarlist.widget.MonthBean
import com.suyf.calendarlist.widget.ScrollSpeedLinearLayoutManger
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val monthList = mutableListOf<MonthBean>()
    val can = Calendar.getInstance()
    val m = can.get(Calendar.MONTH)

    val daynow = can.get(Calendar.DAY_OF_MONTH)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initTestData()
        showMonthList()
    }

    private fun showMonthList() {
        val sc = ScrollSpeedLinearLayoutManger(this)
        rv_date.layoutManager = sc
        val adapter = DateAdapter(monthList)
//        adapter.setItemSelect()
        var pos = -1
        for (i in monthList.indices) {
            if (monthList.get(i).isReport) {
                pos = i
            }
        }
        rv_date.smoothScrollToPosition(pos)
        var m = 0
        adapter.dayItemClickListener = { month, day, view ->
            if (m != month) {
                val dayBean = monthList[m].dayList[day]
                dayBean.isSclete = false
                m = month
                adapter.notifyDataSetChanged()
            }
            val dayBean = monthList[month].dayList[day]

            Toast.makeText(
                this,
                "${dayBean.year}年${dayBean.month + 1}月${dayBean.day}日",
                Toast.LENGTH_SHORT
            ).show()
        }
        rv_date.adapter = adapter
    }

    private fun initTestData() {
        for (year in DateUtils.todayYear downTo 2021) {
            for (month in 11 downTo 0) {
                initMonthList(year, month)
            }
        }
    }

    private fun initMonthList(year: Int, month: Int) {
        val monthBean = findOrCreateMonthBean(year, month)

        val startDay = DateUtils.getMonthStartDay(year, month)
        for (i in 0 until startDay) {
            monthBean.dayList.add(DayBean(year, month, 0))
        }

        val daysInMonth = DateUtils.getDaysInMonth(month, year)
        for (day in 1..daysInMonth) {
            val dayBean = DayBean(year, month, day, DateUtils.isToday(year, month, day))
            if (month <= m) {
                dayBean.isAvailable = true
                if (month == m) {
                    if (day <= daynow)
                        dayBean.isAvailable = true
                    else
                        dayBean.isAvailable = false
                }
            } else {
                dayBean.isAvailable = false
            }
//            dayBean.isAvailable = day % 3 == 0
            monthBean.dayList.add(dayBean)
        }
    }

    private fun findOrCreateMonthBean(year: Int, month: Int): MonthBean {
        val title = "${year}年${month + 1}月"
        var isReport = false
        var dateBean = monthList.find { it.title == title }
        if (dateBean == null) {
            if (year == can.get(Calendar.YEAR) && month == m) {
                isReport = true
            }
            dateBean = MonthBean(title, isReport)
            monthList.add(0, dateBean)
        }
        return dateBean
    }
}
