package com.stanger.listquest.ui.profile.photo_dialog


import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.stanger.listquest.utils.CameraListener

class PhotoDialogFragmentViewModel(
    private val callback: CameraListener,
    private val dialog: Dialog,
    private val context: Context,
    private val frag: Fragment
): ViewModel() {

    private val CAMERA_REQUEST = 111
    private val UPLOAD_REQUEST = 101


    fun onPhotoPressed(){
        if(checkPersmission(CAMERA_REQUEST)) takePhoto()
        else(requestPermission(CAMERA_REQUEST))
    }

    fun onUploadImagePressed(){
        if(checkPersmission(UPLOAD_REQUEST)) uploadPhoto()
        else(requestPermission(UPLOAD_REQUEST))
    }

    fun takePhoto(){
        dialog.dismiss()
        callback.takePhoto()
    }

    fun uploadPhoto(){
        dialog.dismiss()
        callback.uploadPhoto()
    }

    private fun checkPersmission(requestCode: Int): Boolean {
        return when(requestCode){
            CAMERA_REQUEST -> (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
            UPLOAD_REQUEST -> (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            else -> false
        }
    }

    private fun requestPermission(requestCode: Int) {
        if(requestCode == CAMERA_REQUEST) frag.requestPermissions(arrayOf(Manifest.permission.CAMERA), requestCode)
        if(requestCode == UPLOAD_REQUEST) frag.requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), requestCode)
    }
}