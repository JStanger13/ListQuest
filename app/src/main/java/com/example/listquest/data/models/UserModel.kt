package com.example.listquest.data.models

import java.io.Serializable
import java.util.*

data class UserModel(
    var id: String = "",
    var userName: String = "",
    val profilePicturePath: String?,
    var lvl: Int = 0,
    var levelUpDenominator:Int = 1,
    var numQuestsAvail: Int = 0,
    var numQuestsCompleted: Int = 0,
    var levelUpNumerator: Int = 0,
    var isLevelUp: Boolean = false


): Serializable, Observable() {
    constructor() : this(
        "",
        "",
        "",
        1,
        1,
        0,
        0,
        0
    )

    fun getPercent(): Int{
        if (levelUpNumerator == 0){
            return 0
        }
        return(levelUpNumerator/levelUpNumerator).toDouble().toInt()
    }
}



