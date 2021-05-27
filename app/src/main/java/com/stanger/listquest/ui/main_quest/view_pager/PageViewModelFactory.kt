package com.stanger.listquest.ui.main_quest.view_pager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stanger.listquest.data.models.UserModel

class PageViewModelFactory(val userModel: UserModel): ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create(PageViewModel:Class<T>): T {
        return PageViewModel(userModel) as T
    }
}