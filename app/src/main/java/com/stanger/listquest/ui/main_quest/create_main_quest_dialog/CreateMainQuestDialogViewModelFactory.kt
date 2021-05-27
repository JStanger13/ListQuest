package com.stanger.listquest.ui.main_quest.create_main_quest_dialog


import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stanger.listquest.utils.CreateQuestListener


class CreateMainQuestDialogViewModelFactory (
    private val callback: CreateQuestListener,
    private val context: Context,
    private val boss: Int,
    private val res: Resources,
    private val fm: FragmentManager,
    private val dialog: Dialog,
    private val dateTxt: String,
    private val timeTxt: String,
    private val oldName: String


): ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create(SideQuestViewModel:Class<T>): T {
        return CreateMainQuestDialogViewModel(boss, res, fm, context, dialog, callback, dateTxt, timeTxt, oldName) as T
    }
}
