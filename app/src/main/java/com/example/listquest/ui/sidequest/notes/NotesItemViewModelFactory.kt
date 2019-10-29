package com.example.listquest.ui.sidequest.bottomsheet

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.listquest.models.NotesModel

class NotesItemViewModelFactory(
    private val notes: NotesModel,
    private val fm: FragmentManager
): ViewModelProvider.NewInstanceFactory() {

    override fun <T: ViewModel> create(NotesItemViewModel:Class<T>): T {
        return NotesItemViewModel(
            notes,
            fm
        ) as T
    }
}