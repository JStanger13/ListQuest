package com.example.listquest.ui.dialogs.calendar

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.listquest.data.utils.DialogUtils
import com.example.listquest.ui.mainquest.create_main_quest.CreateMainDialogFragment
import java.text.SimpleDateFormat
import java.util.*

class CalenderFragmentViewModel(val callback: CreateMainDialogFragment.CreateQuestListener,
                                val context: Context,
                                val fm: FragmentManager,
                                val dialog: Dialog,
                                private val bossInt: Int,
                                private val dateTxt: String,
                                private val timeTxt: String
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
        //timeString.value = checkIfTimeExists()
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
        return SimpleDateFormat("dd/M/yyyy").format(Date())
    }

    fun timeCancelClicked(){
        timeString.value = "select a time"
        showCancelBtn.value = false
    }

    fun doneClicked(){
        dialog.dismiss()
        DialogUtils.launchCreateMainQuestDialog(fm, CreateMainDialogFragment(callback, bossInt, calendarString.value!!, checkTimeString()))
    }

    fun cancelClicked(){
        dialog.dismiss()
        DialogUtils.launchCreateMainQuestDialog(fm, CreateMainDialogFragment(callback, bossInt, dateTxt, timeTxt))
    }

    private fun checkTimeString(): String{
        return if( timeString.value == "select a time"){
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