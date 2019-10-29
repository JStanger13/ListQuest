package com.example.listquest.models

import java.io.Serializable
import java.util.*

data class UserModel(
    var id: String =  "",
    var lvl: Int = 0,
    var numQuestsAvail: Int = 0,
    var numQuestsCompleted: Int = 0

): Serializable, Observable() {
    constructor() : this("")
}