package com.example.listquest.utils

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.listquest.R
import com.example.listquest.data.models.EventItem
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalenderView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {
    var MAX_EVENT = 3

    companion object {
        const val monthYearFormat = "MMM yyyy"
        const val dateFormat = "dd-MM-yyyy"
        const val dateTimeFormat = "dd-MM-yyyy hh:mm:ss"
        const val EXTRA_MARGIN = 15
        const val POST_TIME: Long = 100
    }

    private var eventList: ArrayList<EventItem> = ArrayList()
    private var calender: Calendar = Calendar.getInstance()
    private var layoutInflater: LayoutInflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var dayViewHeader: View = layoutInflater.inflate(R.layout.layout_day_header_view, this, false)
    private var dayViewRow1: FrameLayout = layoutInflater.inflate(R.layout.layout_day_date_view, this, false) as FrameLayout
    private var dayViewRow2: FrameLayout = layoutInflater.inflate(R.layout.layout_day_date_view, this, false) as FrameLayout
    private var dayViewRow3: FrameLayout = layoutInflater.inflate(R.layout.layout_day_date_view, this, false) as FrameLayout
    private var dayViewRow4: FrameLayout = layoutInflater.inflate(R.layout.layout_day_date_view, this, false) as FrameLayout
    private var dayViewRow5: FrameLayout = layoutInflater.inflate(R.layout.layout_day_date_view, this, false) as FrameLayout
    private var dayViewRow6: FrameLayout = layoutInflater.inflate(R.layout.layout_day_date_view, this, false) as FrameLayout
    private var monthTitle: TextView? = dayViewHeader.findViewById(R.id.txt_monthTitle) as TextView
    private var btn_next: ImageView? = dayViewHeader.findViewById(R.id.btn_next) as ImageView
    private var btn_previous: ImageView? = dayViewHeader.findViewById(R.id.btn_previous) as ImageView

    init {
        removeAllViews()  //In-built function in LinearLayout
        orientation = LinearLayout.VERTICAL
        addView(dayViewHeader)
        addView(dayViewRow1)
        addView(dayViewRow2)
        addView(dayViewRow3)
        addView(dayViewRow4)
        addView(dayViewRow5)
        addView(dayViewRow6)
        monthTitle?.text = SimpleDateFormat(monthYearFormat, Locale.getDefault()).format(calender.time)
        btn_next?.setOnClickListener { nextMonth() }
        btn_previous?.setOnClickListener { previousMonth() }

        setOnTouchListener(object : OnCalendarSwipeListener(context!!) {
            override fun onSwipeLeft() {
                nextMonth()
            }
            override fun onSwipeRight() {
                previousMonth()
            }
        })
        updateEventView(true)
    }
    fun nextMonth() {
        calender.add(Calendar.MONTH, 1)
        monthTitle?.text = SimpleDateFormat(monthYearFormat, Locale.getDefault()).format(calender.time)
        updateEventView()
    }
    fun previousMonth() {
        calender.add(Calendar.MONTH, -1)
        monthTitle?.text = SimpleDateFormat(monthYearFormat, Locale.getDefault()).format(calender.time)
        updateEventView()
    }
    private fun updateEventView(firstTime: Boolean = false) {
        //Will implement this in next Part, Part-2
    }
}