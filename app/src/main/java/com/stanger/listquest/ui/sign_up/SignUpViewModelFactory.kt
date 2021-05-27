package com.stanger.listquest.ui.sign_up


import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SignUpViewModelFactory(
    private val fm: FragmentManager,
    private val context: Context,
    private val loginPreferences: SharedPreferences
): ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create(CreateAccountViewModel:Class<T>): T {
        return SignUpViewModel(fm, context, loginPreferences) as T
    }
}