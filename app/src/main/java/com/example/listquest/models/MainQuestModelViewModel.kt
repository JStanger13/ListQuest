package com.example.listquest.models

import androidx.lifecycle.ViewModel

abstract class MainQuestViewModel<MainQuestModel>: ViewModel() {
    var mainQuestModel: MainQuestModel
    abstract fun setItem(item: MainQuestModel){

    }
}
