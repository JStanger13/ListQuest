package com.stanger.listquest.ui.calendar


import android.app.Dialog
import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stanger.listquest.data.models.MainQuestModel
import com.stanger.listquest.utils.CreateQuestListener


class CalenderFragmentViewModelFactory (
    private val callback: CreateQuestListener,
    private val context: Context,
    private val fm: FragmentManager,
    private val dialog: Dialog,
    private val bossInt: Int,
    private val dateTxt: String,
    private val timeTxt: String,
    private val isEditDialog: Boolean,
    private val  editMainQuestModel: MainQuestModel,
    private val oldName: String

): ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create(SideQuestViewModel:Class<T>): T {
        return CalenderFragmentViewModel(callback, context, fm, dialog, bossInt, dateTxt, timeTxt, isEditDialog, editMainQuestModel, oldName) as T
    }
}