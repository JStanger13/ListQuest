package com.example.listquest.ui.dialogs.level_up

import android.app.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LevelUpViewModelFactory(val dialog: Dialog): ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create(LevelUpViewModel:Class<T>): T {
        return LevelUpViewModel(dialog) as T
    }
}