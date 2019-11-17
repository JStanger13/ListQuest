package com.example.listquest.ui.dialogs

import android.app.Dialog
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.listquest.data.models.MainQuestModel
import com.example.listquest.data.models.NotesModel

class EditNotesDialogViewModelFactory(
    private var notes: NotesModel,
    private val fm: FragmentManager,
    private val dialog: Dialog,
    private val mainQuest: MainQuestModel,
    private var callback: EditNotesDialogFragment.EditNotesListener

): ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create(EditNotesDialogViewModel:Class<T>): T {
        return EditNotesDialogViewModel(notes, fm, dialog, mainQuest, callback) as T
    }
}
