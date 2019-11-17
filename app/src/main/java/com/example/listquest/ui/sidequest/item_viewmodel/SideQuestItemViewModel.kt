package com.example.listquest.ui.sidequest.item_viewmodel

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.example.listquest.data.models.MainQuestModel
import com.example.listquest.data.models.SideQuestModel
import com.example.listquest.data.utils.DatabaseUtils.Companion.firebaseRepository

class SideQuestItemViewModel(val mainQuestModel: MainQuestModel,
                             var sideQuestModel: SideQuestModel): BaseObservable() {

    private var _isChecked = sideQuestModel.isChecked

    var ischecked: Boolean
        @Bindable get() = _isChecked
        @Bindable set(value) {
            _isChecked = value
            editSideQuestChk(value)
            notifyPropertyChanged(BR.ischecked)
        }

    fun getTitle(): String {
        return sideQuestModel.sideQuestTitle
    }

    private fun editSideQuestChk(newCheck: Boolean) {
        firebaseRepository.editSideQuestChk(mainQuestModel, sideQuestModel, newCheck)
    }

}