package com.example.listquest.models

import java.util.*

open class BottomSheetItem(val itemType: Int) {

    class TYPE {
        companion object {
            val Notes = 0
            val Calendar = 1
            private var expanded: Boolean = false

        }
    }
}