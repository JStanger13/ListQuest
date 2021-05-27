package com.stanger.listquest.ui.side_quest.create_side_quest_dialog


import android.app.Dialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stanger.listquest.utils.CreateSideQuestListener

class CreateSideQuestDialogViewModel(var id: String,
                                     val callback: CreateSideQuestListener,
                                     val dialog: Dialog
): ViewModel() {
    var newSideName = MutableLiveData<String>()

    init {
        newSideName.value = ""
    }

    fun createButton(){
        if(newSideName.value!!.isNotEmpty()) {
            callback.addSideQuestFromDialog(id, newSideName.value!!)
            dialog.dismiss()
        }
    }
}