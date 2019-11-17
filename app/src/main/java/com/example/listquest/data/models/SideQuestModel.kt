package com.example.listquest.data.models

import com.google.firebase.Timestamp
import java.io.Serializable
import java.util.*

data class SideQuestModel(
    var id: String = "",
    var sideQuestTitle: String = "",
    var isChecked: Boolean = false,
    var timestamp: Timestamp) : Serializable, Observable() {

    constructor() : this(
        "",
        "",
        false,
        Timestamp.now())
}

