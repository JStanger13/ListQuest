package com.example.listquest.models

import java.io.Serializable

class MainQuestModel(var mainQuestTitle: String,
                     var boss: String,
                     var badge: String,
                     var bossHealth: Int,
                     var sideQuestList: MutableList<SideQuestModel>): Serializable