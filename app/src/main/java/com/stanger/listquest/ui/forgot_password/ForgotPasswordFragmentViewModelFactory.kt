package com.stanger.listquest.ui.forgot_password

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ForgotPasswordFragmentViewModelFactory(val context: Context,
                                             val fm: FragmentManager):
    ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create(ForgotPasswordFragmentViewModel:Class<T>): T {
        return ForgotPasswordFragmentViewModel(context, fm) as T
    }
}