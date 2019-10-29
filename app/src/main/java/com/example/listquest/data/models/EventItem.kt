package com.example.listquest.models

import java.text.SimpleDateFormat
import java.util.*

data class EventItem(var start: String, var end: String, var title: String = "My Event", var color: String = "#00FFFF", var id: String = "") {
    fun getStartDateToSort(): Date {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return sdf.parse(start)
    }
}