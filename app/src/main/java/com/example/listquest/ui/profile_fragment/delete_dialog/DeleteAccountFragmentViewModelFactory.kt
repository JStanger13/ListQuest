package com.example.listquest.ui.profile_fragment.delete_dialog

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.listquest.data.models.UserModel

class DeleteDialogViewModelFactory(
    private val fm: FragmentManager,
    private val userModel: UserModel,
    private val context: Context

) : ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create(SideQuestViewModel:Class<T>): T {
        return DeleteDialogViewModel(fm, userModel, context) as T
    }
}