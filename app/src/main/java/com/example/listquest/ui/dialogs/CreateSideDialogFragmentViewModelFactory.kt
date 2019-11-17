//package com.example.listquest.ui.dialogs
//
//import android.app.Dialog
//import android.content.Context
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//
//class CreateSideDialogFragmentViewModelFactory(
//    private var id: String,
//    private val callback: CreateSideDialogFragment.CreateSideQuestListener,
//    private val dialog: Dialog
//
//): ViewModelProvider.NewInstanceFactory() {
//    override fun <T: ViewModel> create(SideQuestViewModel:Class<T>): T {
//        return CreateSideDialogViewModel(id, callback, dialog) as T
//    }
//}
