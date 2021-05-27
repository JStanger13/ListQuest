package com.stanger.listquest.ui.profile


import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.stanger.listquest.R
import com.stanger.listquest.data.models.UserModel
import com.stanger.listquest.ui.delete_account.DeleteAccountFragment
import com.stanger.listquest.ui.profile.photo_dialog.PhotoDialogFragment
import com.stanger.listquest.utils.CameraListener
import com.stanger.listquest.utils.DialogUtils
import com.stanger.listquest.utils.FirestoreRepository


class ProfileFragmentViewModel(
    val userModel: UserModel,
    val fm: FragmentManager,
    userPercent: Int,
    val callback: CameraListener,
    val context: Context
) : ViewModel() {

    var firebaseRepository = FirestoreRepository()
    var profilePicturePath = MutableLiveData<String>()
    var errorImg = MutableLiveData<Int>()
    var userPercentProgress = MutableLiveData<Int>()
    var userPercentText = MutableLiveData<String>()
    var userName = MutableLiveData<String>()
    var userEmail = MutableLiveData<String>()
    val userAboutMe = MutableLiveData<String>()
    val isProgressBarLoading = MutableLiveData<Boolean>()

    init {
        errorImg.value = R.drawable.profile_no_img
        profilePicturePath.value = userModel.profilePicturePath
        userPercentProgress.value = userPercent
        userPercentText.value = "level " + userModel.lvl.toString()
        userName.value = userModel.name
        userEmail.value = userModel.email
        userAboutMe.value = userModel.aboutMe
        profilePicturePath.value = userModel.profilePicturePath
        getImgFromFirestore(userModel.profilePicturePath)
    }

    private fun getImgFromFirestore(path: String) {
        isProgressBarLoading.value = true
        if (userModel.isImgFromGoogleOrFacebook){
            profilePicturePath.value = path
            isProgressBarLoading.value = false
        }
        else {
            FirebaseStorage.getInstance().reference.child("photos").child(path)
                .downloadUrl.addOnSuccessListener {
                isProgressBarLoading.value = false
                profilePicturePath.value = it.toString()
            }.addOnFailureListener {
                isProgressBarLoading.value = false
            }
        }
    }

    fun launchPhotoDialog() = DialogUtils.launchPhotoDialog(
        fm,
        PhotoDialogFragment(
            userModel,
            fm,
            callback
        )
    )

    fun dismissDialog() = fm.popBackStack()

    fun logout() {
        isProgressBarLoading.value = true
        DialogUtils.launchLogInFragment(fm)
        FirebaseAuth.getInstance().signOut()
        firebaseRepository.deleteSnapMainCompleted(userModel)


        isProgressBarLoading.value = false
    }

    fun deleteAccount() = DialogUtils.launchDeleteDialog(fm, DeleteAccountFragment(userModel))
}