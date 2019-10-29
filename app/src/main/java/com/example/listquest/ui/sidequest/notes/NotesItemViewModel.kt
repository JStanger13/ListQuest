package com.example.listquest.ui.sidequest.bottomsheet

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import com.example.listquest.models.NotesModel
import com.example.listquest.ui.dialogs.EditNotesDialogFragment
import com.example.listquest.utils.DialogUtils

class NotesItemViewModel(var notes: NotesModel,
                         var fm: FragmentManager): ViewModel(),
    EditNotesDialogFragment.EditNotesListener {

    override fun editNotesFromDialog(notes: NotesModel) {
    }

    fun launchEditNotesDialog(){
        DialogUtils.launchEditNotesDialog(fm, EditNotesDialogFragment(this).newInstance(notes))
    }
}