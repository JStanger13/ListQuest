package com.example.listquest.ui.mainquest

import android.content.Context
import android.content.res.Resources
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainQuestViewModelFactory (private val res: Resources,
                                 val  fm: FragmentManager): ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create(MainQuestViewModel:Class<T>): T {
        return MainQuestViewModel(res, fm) as T
    }
}
