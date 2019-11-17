package com.example.listquest.ui.mainquest.create_main_quest

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CreateMainDialogFragmentViewModelFactory (
    private val callback: CreateMainDialogFragment.CreateQuestListener,
    private val context: Context,
    private val boss: Int,
    private val res: Resources,
    private val fm: FragmentManager,
    private val dialog: Dialog,
    private val dateTxt: String,
    private val timeTxt: String

): ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create(SideQuestViewModel:Class<T>): T {
        return CreateMainDialogFragmentViewModel(boss, res, fm, context, dialog, callback, dateTxt, timeTxt) as T
    }
}
