package com.example.listquest.ui.dialogs.calendar

import android.app.Dialog
import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.listquest.ui.mainquest.create_main_quest.CreateMainDialogFragment

class CalenderFragmentViewModelFactory (
    private val callback: CreateMainDialogFragment.CreateQuestListener,
    private val context: Context,
    private val fm: FragmentManager,
    private val dialog: Dialog,
    private val bossInt: Int,
    private val dateTxt: String,
    private val timeTxt: String

): ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create(SideQuestViewModel:Class<T>): T {
        return CalenderFragmentViewModel(callback, context, fm, dialog, bossInt, dateTxt, timeTxt) as T
    }
}