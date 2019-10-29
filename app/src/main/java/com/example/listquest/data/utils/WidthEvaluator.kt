package com.example.listquest.utils

import android.animation.IntEvaluator
import android.view.View


class WidthEvaluator(val v: View): IntEvaluator() {

    override fun evaluate(fraction: Float, startValue: Int, endValue: Int): Int {
        val num = super.evaluate(fraction, startValue, endValue)!!
        val params = v.layoutParams
        params.width = num
        v.setLayoutParams(params)
        return num
    }
}