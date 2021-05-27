package com.stanger.listquest.ui.side_quest.side_quest_item

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import com.stanger.listquest.BR
import com.stanger.listquest.MainActivity.Companion.firebaseRepository
import com.stanger.listquest.data.models.MainQuestModel
import com.stanger.listquest.data.models.SideQuestModel
import com.stanger.listquest.data.models.UserModel
import com.stanger.listquest.ui.side_quest.edit_side_quest_dialog.EditSideQuestDialogFragment
import com.stanger.listquest.utils.DialogUtils
import com.stanger.listquest.utils.SideEditQuestListener


class SideQuestItemViewModel(
    private val mainQuestModel: MainQuestModel,
    val fm: FragmentManager,
    var sideQuestModel: SideQuestModel,
    val callback: SideEditQuestListener,
    val userModel: UserModel
): BaseObservable() {

    private var _isChecked = sideQuestModel.isChecked
    val title = MutableLiveData<String>()
    var editIsFocusable = MutableLiveData<Boolean>()
    init {
        title.value = sideQuestModel.sideQuestTitle
        editIsFocusable.value = false
    }

    var ischecked: Boolean
        @Bindable get() = _isChecked
        @Bindable set(value) {
            _isChecked = value
            editSideQuestChk(value)
//            notifyPropertyChanged(BR.isChecked)
        }

    fun getTitle(): String {
        return sideQuestModel.sideQuestTitle
    }

    fun onSideClicked(){
        editIsFocusable.value = true
    }

    private fun editSideQuestChk(newCheck: Boolean) {
        firebaseRepository.editSideQuestChk(mainQuestModel, sideQuestModel, newCheck,  userModel)
        if(mainQuestModel.favorite) firebaseRepository.editFavoriteChk(mainQuestModel, sideQuestModel, newCheck, userModel)
    }

    fun launchEditDialog(){
        if(!_isChecked) DialogUtils.launchEditSideDialog(fm, EditSideQuestDialogFragment(callback).newInstance(sideQuestModel))
    }
}