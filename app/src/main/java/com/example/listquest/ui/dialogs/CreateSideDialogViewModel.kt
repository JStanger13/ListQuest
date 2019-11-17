//package com.example.listquest.ui.dialogs
//
//import android.app.Dialog
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.facebook.internal.Mutable
//
//class CreateSideDialogViewModel(var id: String,
//                                val callback: CreateSideDialogFragment.CreateSideQuestListener,
//                                val dialog: Dialog
//): ViewModel() {
//    var newSideName = MutableLiveData<String>()
//
//    fun createButton(){
//        if(newSideName.value!!.isNotEmpty()) {
//            callback.addSideQuestFromDialog(id, newSideName.value!!)
//            dialog.dismiss()
//        }
//    }
//}