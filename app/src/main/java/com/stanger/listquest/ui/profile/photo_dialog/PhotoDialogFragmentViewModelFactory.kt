package com.stanger.listquest.ui.profile.photo_dialog


import android.app.Dialog
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stanger.listquest.utils.CameraListener

class PhotoDialogFragmentViewModelFactory (
    private val callback: CameraListener,
    private val dialog: Dialog,
    private val context: Context,
    private val frag: Fragment
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(PhotoDialogFragmentViewModel: Class<T>): T {
        return PhotoDialogFragmentViewModel(
            callback,
            dialog,
            context,
            frag
        ) as T
    }
}