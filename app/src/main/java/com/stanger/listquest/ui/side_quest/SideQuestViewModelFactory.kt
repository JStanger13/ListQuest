package com.stanger.listquest.ui.side_quest

import android.content.Context
import android.content.res.Resources
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stanger.listquest.data.models.MainQuestModel
import com.stanger.listquest.data.models.UserModel


class SideQuestViewModelFactory(
    private val currentMainQuestModel: MainQuestModel,
    private val resources: Resources,
    private val fm: FragmentManager,
    private val context: Context,
    private val userModel: UserModel
): ViewModelProvider.NewInstanceFactory() {

    override fun <T: ViewModel> create(SideQuestViewModel:Class<T>): T {
        return SideQuestViewModel(
            currentMainQuestModel,
            resources,
            fm,
            context,
            userModel
        ) as T
    }
}