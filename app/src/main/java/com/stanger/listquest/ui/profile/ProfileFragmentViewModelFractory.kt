package com.stanger.listquest.ui.profile


import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stanger.listquest.data.models.UserModel
import com.stanger.listquest.utils.CameraListener


class ProfileFragmentViewModelFractory(
    private val userModel: UserModel,
    private val fm: FragmentManager,
    private var userPercent: Int,
    private val callback: CameraListener,
    private val context: Context

): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(ProfileFragmentViewModel: Class<T>): T {
        return ProfileFragmentViewModel(userModel, fm, userPercent, callback, context) as T
    }
}