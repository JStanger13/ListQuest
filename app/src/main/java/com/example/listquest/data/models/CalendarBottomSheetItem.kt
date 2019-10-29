package com.example.listquest.models

class CalendarBottomSheetItem: BottomSheetItem(TYPE.Calendar) {
    fun getType(): Int{
        return TYPE.Calendar
    }
}