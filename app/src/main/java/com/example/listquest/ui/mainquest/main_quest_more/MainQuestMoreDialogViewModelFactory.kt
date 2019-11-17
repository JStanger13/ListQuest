package com.example.listquest.ui.mainquest.main_quest_more

import android.app.Dialog
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainQuestMoreDialogViewModelFactory(
    private val fm: FragmentManager,
    private val dialog: Dialog

): ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create(MainQuestMoreDialogViewModel:Class<T>): T {
        return MainQuestMoreDialogViewModel(fm, dialog) as T
    }
}
