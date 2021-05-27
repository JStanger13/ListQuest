package com.stanger.listquest.ui.login

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stanger.listquest.utils.ErrorListener

class LoginFragmentViewModelFactory(private val fm: FragmentManager,
                                    private val context: Context,
                                    private val callback: ErrorListener,
                                    private val loginPreferences: SharedPreferences
):
    ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create(LoginFragmentViewModel:Class<T>): T {
        return LoginFragmentViewModel(fm, context, callback, loginPreferences) as T
    }
}
