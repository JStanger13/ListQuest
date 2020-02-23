package com.stanger.listquest.ui.main_quest.create_main_quest_dialog


import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.google.firebase.Timestamp
import com.stanger.listquestandroid.MainActivity.Companion.firebaseRepository
import com.stanger.listquestandroid.R
import com.stanger.listquestandroid.data.models.MainQuestModel
import com.stanger.listquestandroid.ui.calendar.CalenderFragment
import com.stanger.listquestandroid.utils.BossUtils
import com.stanger.listquestandroid.utils.CreateQuestListener
import com.stanger.listquestandroid.utils.DialogUtils
import com.stanger.listquestandroid.utils.KeyboardUtils


class CreateMainQuestDialogViewModel(
    private val boss: Int,
    val res: Resources,
    val fm: FragmentManager,
    val context: Context,
    val dialog: Dialog,
    val callback: CreateQuestListener,
    private var dateTxt: String,
    private var timeTxt: String,
    oldName: String) : ViewModel() {

    var bossImg = MutableLiveData<Drawable>()
    var bossTitle = MutableLiveData<String>()
    var newName = MutableLiveData<String>()
    var newBossName = BossUtils.getBossName(boss)
    var calenderIsShown = MutableLiveData<Boolean>()
    var dateString = MutableLiveData<String>()
    var dateCancel = MutableLiveData<Boolean>()

    init {
        newName.value = oldName
        calenderIsShown.value = false
        bossImg.value = res.getDrawable(BossUtils.changeBossImage(boss), null)
        bossTitle.value = BossUtils.getBossName(boss)
        dateString.value = checkIfDateIsSet() + dateTxt + checkIfTimeIsSet()
        showCancel()
    }

    private fun showCancel(){
        dateCancel.value = dateString.value!!.isNotEmpty()
    }

    private fun checkIfTimeIsSet(): String {
        if(timeTxt.isEmpty()){
            return timeTxt
        }
        return ", $timeTxt"
        dateCancel.value = true
    }

    private fun checkIfDateIsSet(): String {
        if(dateTxt.isEmpty()){
            return ""
        }
        return "due:  "
    }

    fun createButton() {
        if (newName.value!!.isNotEmpty()) {
            val main = MainQuestModel(
                firebaseRepository.getMainQuestId(),
                newName.value!!,
                newBossName,
                boss,
                "",
                0,
                0,
                0,
                Timestamp.now(),
                "",
                false,
                dateTxt,
                timeTxt)
            callback.addMainQuestFromDialog(main)
            dialog.dismiss()
            KeyboardUtils.hideSoftKeyBoard(dialog.findViewById(R.id.edit_text_name))
        }
    }

    fun cancelDateAndTime(){
        dateTxt = ""
        timeTxt = ""
        dateString.value = ""
        showCancel()
    }

    fun calendarClick(v: View) {
        dialog.dismiss()
        KeyboardUtils.hideSoftKeyBoard(v)
        DialogUtils.launchCalendarDialog(fm, CalenderFragment(callback, boss, dateTxt, timeTxt, false, MainQuestModel(), newName.value!!).newInstance())
    }
}