package com.stanger.listquest.utils

import com.stanger.listquest.data.models.MainQuestModel
import com.stanger.listquest.data.models.SideQuestModel


interface CreateQuestListener {
    fun addMainQuestFromDialog(mainQuestModel: MainQuestModel)
    fun editMainQuestFromDialog(mainQuestModel: MainQuestModel)
}

interface EditQuestListener {
    fun editQuestFromDialog(mainQuestModel: MainQuestModel)
}

interface FavoriteListener{
    fun addMainToFavorites(){}
}

interface CameraListener {
    fun takePhoto(){}
    fun uploadPhoto(){}
    fun passPhoto(path: String){}
}

interface CreateSideQuestListener {
    fun addSideQuestFromDialog(id: String, newSideQuestTitle: String)
}

interface SideEditQuestListener {
    fun editSideQuestFromDialog(sideQuest: SideQuestModel)
}

interface ErrorListener{
    fun checkEditTextError(){}
}