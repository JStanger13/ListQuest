package com.example.listquest.ui.dialogs

import android.app.Dialog
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import com.example.listquest.data.models.UserModel

class PhotoDialogFragmentViewModel(val userModel: UserModel,
                                   val fm: FragmentManager,
                                   val callback: CameraListener,
                                   val dialog: Dialog
): ViewModel() {

    interface CameraListener {
        fun takePhoto(){}
        fun uploadPhoto(){}
        fun passPhoto(path: String){}
    }

    fun takePhoto(){
        dialog.dismiss()
        callback.takePhoto()
    }

    fun uploadPhoto(){
        dialog.dismiss()
        callback.uploadPhoto()
    }
}