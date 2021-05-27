package com.stanger.listquest.ui.calendar


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stanger.listquest.data.models.MainQuestModel
import com.stanger.listquest.ui.main_quest.create_main_quest_dialog.CreateMainQuestDialogFragment
import com.stanger.listquest.utils.CreateQuestListener
import com.stanger.listquest.utils.DialogUtils

import java.text.SimpleDateFormat
import java.util.*

class CalenderFragmentViewModel(val callback: CreateQuestListener,
                                val context: Context,
                                val fm: FragmentManager,
                                val dialog: Dialog,
                                private val bossInt: Int,
                                private val dateTxt: String,
                                private val timeTxt: String,
                                private val isEditDialog: Boolean,
                                var editMainQuestModel: MainQuestModel,
                                private val oldName: String
) : ViewModel() {

    val calendarIsOpen = MutableLiveData<Boolean>()
    private val timePickerIsOpen = MutableLiveData<Boolean>()
    val calendarString = MutableLiveData<String>()
    val timeString = MutableLiveData<String>()
    var currentTime = ""
    var showCancelBtn = MutableLiveData<Boolean>()

    init {
        calendarIsOpen.value = true
        timePickerIsOpen.value = true
        calendarString.value = getCurrentDateString()
        checkIfTimeExists()
    }

    private fun checkIfTimeExists(){
        if(timeTxt.isEmpty()){
            timeString.value = "select a time"
            showCancelBtn.value = false
        } else {
            timeString.value = timeTxt
            showCancelBtn.value = true
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDateString(): String{
        return SimpleDateFormat("M/d/yyyy").format(Date())
    }

    fun timeCancelClicked(){
        timeString.value = "select a time"
        showCancelBtn.value = false
    }

    fun doneClicked(){
        dialog.dismiss()
        if(isEditDialog){
            editMainQuestModel.eventDate = calendarString.value!!
            editMainQuestModel.eventTime = checkTimeString()
            callback.editMainQuestFromDialog(editMainQuestModel)
        }
        else DialogUtils.launchCreateMainQuestDialog(fm, CreateMainQuestDialogFragment(callback, bossInt, calendarString.value!!, checkTimeString(), oldName))
    }

    fun cancelClicked(){
        dialog.dismiss()
        if(!isEditDialog) DialogUtils.launchCreateMainQuestDialog(fm, CreateMainQuestDialogFragment(callback, bossInt, dateTxt, timeTxt, oldName))
    }

    private fun checkTimeString(): String{
        return if(timeString.value == "select a time"){
            ""
        } else {
            timeString.value!!
        }
    }

    fun calendarOnClick(){
        showCancelBtn.value = timeString.value != "select a time"
        calendarIsOpen.value = true
    }

    fun timePickerOnClick(){
        timeString.value = currentTime
        showCancelBtn.value = false
        calendarIsOpen.value = false
    }
}