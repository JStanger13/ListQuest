package com.example.listquest.ui.sidequest.create_side_quest

import android.app.Dialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CreateSideDialogViewModel(var id: String,
                                val callback: CreateSideDialogFragment.CreateSideQuestListener,
                                val dialog: Dialog
): ViewModel() {
    var newSideName = MutableLiveData<String>()

    fun createButton(){
        if(newSideName.value!!.isNotEmpty()) {
            callback.addSideQuestFromDialog(id, newSideName.value!!)
            dialog.dismiss()
        }
    }
}