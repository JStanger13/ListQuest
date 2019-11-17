package com.example.listquest.ui.dialogs

import android.app.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NotesDialogFragmentViewModelFactory (
    private val callback: NotesDialogFragment.NotesListener,
    private val id: String,
    private val dialog: Dialog,
    private val title: String,
    private val body: String

    ): ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create(NotesDialogFragmentViewModel:Class<T>): T {
        return NotesDialogFragmentViewModel(id, callback, dialog, title, body) as T
    }
}
