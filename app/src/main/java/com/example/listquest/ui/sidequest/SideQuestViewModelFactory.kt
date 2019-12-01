package com.example.listquest.ui.sidequest.item_viewmodel

import android.content.Context
import android.content.res.Resources
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.listquest.data.models.MainQuestModel

class SideQuestViewModelFactory(
    private val currentMainQuestModel: MainQuestModel,
    private val resources: Resources,
    private val fm: FragmentManager,
    private val context: Context): ViewModelProvider.NewInstanceFactory() {

    override fun <T: ViewModel> create(SideQuestViewModel:Class<T>): T {
        return com.example.listquest.ui.sidequest.SideQuestViewModel(
            currentMainQuestModel,
            resources,
            fm,
            context
        ) as T
    }
}