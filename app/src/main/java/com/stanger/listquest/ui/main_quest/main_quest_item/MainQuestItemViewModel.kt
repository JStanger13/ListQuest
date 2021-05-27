package com.stanger.listquest.ui.main_quest.main_quest_item

import android.content.res.Resources
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import com.stanger.listquest.BR
import com.stanger.listquest.data.models.MainQuestModel

class MainQuestItemViewModel(mainQuestModel: MainQuestModel, val res: Resources): BaseObservable() {

    private var _isChecked = mainQuestModel.completed
    val showNotes = MutableLiveData<Boolean>()

    init {
        showNotes.value = mainQuestModel.notes.isNotEmpty()
    }

//    var isChecked: Boolean
//        @Bindable get() = _isChecked
//        @Bindable set(value) {
//            _isChecked = value
//            notifyPropertyChanged(BR.checked)
//        }
}