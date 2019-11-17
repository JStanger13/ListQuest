package com.example.listquest.ui.mainquest.create_main_quest

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.listquest.R
import com.example.listquest.data.utils.BossUtils
import com.example.listquest.data.utils.DialogUtils
import com.example.listquest.data.utils.KeyboardUtils
import com.example.listquest.ui.dialogs.calendar.CalenderFragment


class CreateMainDialogFragmentViewModel(
    private val boss: Int,
    val res: Resources,
    val fm: FragmentManager,
    val context: Context,
    val dialog: Dialog,
    val callback: CreateMainDialogFragment.CreateQuestListener,
    private var dateTxt: String,
    private var timeTxt: String) : ViewModel() {

    var bossImg = MutableLiveData<Drawable>()
    var bossTitle = MutableLiveData<String>()
    var newName = MutableLiveData<String>()
    var newBossName = BossUtils.getBossName(boss)
    var calenderIsShown = MutableLiveData<Boolean>()
    var dateString = MutableLiveData<String>()
    var dateCancel = MutableLiveData<Boolean>()

    init {
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
            callback.addMainQuestFromDialog(newName.value!!, boss, newBossName, dateTxt, timeTxt)
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
        DialogUtils.launchCalendarDialog(fm, CalenderFragment(callback, boss, dateTxt, timeTxt).newInstance())
    }
}