package com.stanger.listquest.ui.side_quest.create_side_quest_dialog

import android.app.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stanger.listquest.utils.CreateSideQuestListener

class CreateSideQuestDialogViewModelFactory(
    private var id: String,
    private val callback: CreateSideQuestListener,
    private val dialog: Dialog

): ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create(SideQuestViewModel:Class<T>): T {
        return CreateSideQuestDialogViewModel(id, callback, dialog) as T
    }
}