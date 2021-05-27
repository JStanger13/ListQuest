package com.stanger.listquest.ui.side_quest.edit_side_quest_dialog

import android.app.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stanger.listquest.data.models.SideQuestModel
import com.stanger.listquest.utils.SideEditQuestListener


class EditSideQuestDialogViewModelFactory(
    private var sideQuestModel: SideQuestModel,
    private var dialog: Dialog,
    private val callback: SideEditQuestListener

): ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create(EditSideDialogViewModel:Class<T>): T {
        return EditSideQuestDialogViewModel(sideQuestModel, dialog, callback) as T
    }
}
