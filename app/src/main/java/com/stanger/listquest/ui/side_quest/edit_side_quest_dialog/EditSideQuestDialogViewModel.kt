package com.stanger.listquest.ui.side_quest.edit_side_quest_dialog


import android.app.Dialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stanger.listquest.data.models.SideQuestModel
import com.stanger.listquest.utils.SideEditQuestListener

class EditSideQuestDialogViewModel(val sideQuest: SideQuestModel,
                                   val dialog: Dialog,
                                   val callback: SideEditQuestListener
) : ViewModel() {
    val title = MutableLiveData<String>()

    init {
        title.value = sideQuest.sideQuestTitle
        //var firebaseRepository = FirestoreRepository()
    }

    fun editButton(){
        sideQuest.sideQuestTitle = title.value!!
        callback.editSideQuestFromDialog(sideQuest)
        dialog.dismiss()
    }
}