package com.example.listquest.ui.dialogs.level_up

import android.app.Dialog
import androidx.lifecycle.ViewModel

class LevelUpViewModel(val dialog: Dialog) : ViewModel() {

    fun clickDismiss(){
        dialog.dismiss()
    }
}