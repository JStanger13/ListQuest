package com.stanger.listquest.ui.main_quest.view_pager


import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class LockableViewPager : ViewPager {
    private var swipeable = false

    constructor(context: Context?) : super(context!!) {}

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        swipeable = false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (swipeable) {
            super.onTouchEvent(event)
        } else false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (swipeable) {
            super.onInterceptTouchEvent(event)
        } else false
    }
}