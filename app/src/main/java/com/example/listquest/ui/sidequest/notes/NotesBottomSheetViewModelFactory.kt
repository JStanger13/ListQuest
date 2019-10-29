package com.example.listquest.ui.sidequest.bottomsheet

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.listquest.models.MainQuestModel

class NotesBottomSheetViewModelFactory(
    private val currentMainQuestModel: MainQuestModel,
    private val fm: FragmentManager
): ViewModelProvider.NewInstanceFactory() {

    override fun <T: ViewModel> create(NotesBottomSheetViewModel:Class<T>): T {
        return NotesBottomSheetViewModel(
            currentMainQuestModel,
            fm
        ) as T
    }
}