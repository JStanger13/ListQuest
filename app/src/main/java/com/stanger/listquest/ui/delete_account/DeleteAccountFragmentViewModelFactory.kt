package com.stanger.listquest.ui.delete_account


import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stanger.listquest.data.models.UserModel

class DeleteAccountFragmentViewModelFactory(
    private val fm: FragmentManager,
    private val userModel: UserModel,
    private val context: Context,
    private val loginPreferences: SharedPreferences

) : ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create(SideQuestViewModel:Class<T>): T {
        return DeleteAccountFragmentViewModel(fm, userModel, context, loginPreferences) as T
    }
}