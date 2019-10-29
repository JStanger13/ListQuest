package com.example.listquest.models

import com.google.firebase.Timestamp
import java.io.Serializable
import java.util.*

data class MainQuestModel(
    var id: String =  "",
    var mainQuestTitle: String =  "",
    var boss: String  =  "",
    var bossImg: Int = 0,
    var badge: String = "",
    var bossHealth: Int =  0,
    var questSize: Int = 0,
    var bossPercent: Int = 0,
    var timestamp: Timestamp,
    var notes: String,
    var completed: Boolean = false

): Serializable, Observable() {
    constructor() : this("",
        "",
        "",
        0,
        "",
        0,
        0,
        0,
        Timestamp.now(),
        "",
        false)
}
