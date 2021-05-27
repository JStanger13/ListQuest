package com.stanger.listquest.ui.delete_account


import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.stanger.listquest.data.models.UserModel
import com.stanger.listquest.ui.forgot_password.ForgotPasswordFragment
import com.stanger.listquest.ui.side_quest.SideQuestViewModel.Companion.TAG
import com.stanger.listquest.utils.DialogUtils
import com.stanger.listquest.utils.FirestoreRepository


class DeleteAccountFragmentViewModel(val fm: FragmentManager,
                                     val userModel: UserModel,
                                     val context: Context,
                                     val loginPreferences: SharedPreferences
): ViewModel() {
    var firebaseRepository = FirestoreRepository()
    val user = FirebaseAuth.getInstance().currentUser
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val errorEmail = MutableLiveData<String>()
    val errorPassword = MutableLiveData<String>()
    val isProgressBarLoasing = MutableLiveData<Boolean>()
    val loginPrefsEditor = loginPreferences.edit()

    init {
        email.value = ""
        password.value = ""
    }

    fun doneClicked() {
        fm.popBackStack()
    }

    fun deleteUser() {
        isProgressBarLoasing.value = true
        if(!email.value.isNullOrEmpty() &&  !password.value.isNullOrEmpty()) {
            val credential = EmailAuthProvider.getCredential(email.value!!, password.value!!)
            user?.reauthenticate(credential)
                ?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d(TAG, "User re-authenticated.")
                        deleteAllFields(user)
                    } else {
                        isProgressBarLoasing.value = false
                        Toast.makeText(
                            context,
                            "Make sure you have the correct email.",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d(TAG, "ERROR  User re-authenticated.")
                    }
                }
        }else{
            isProgressBarLoasing.value = false
            Toast.makeText(
                context,
                "Please type in your credentials!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun forgotPassword(){
        fm.popBackStack()
        DialogUtils.launchForgotPasswordFragment(fm, ForgotPasswordFragment())
    }


    private fun deleteAllFields(firebaseUser: FirebaseUser) {
        firebaseRepository.deleteSnapMain(userModel)
        firebaseRepository.deleteSnapMainFavorite(userModel)
        firebaseRepository.deleteSnapMainCompleted(userModel)
        firebaseRepository.deleteSnapImagesCompleted(userModel)
        firebaseRepository.storageRef.child("photos").child(userModel.profilePicturePath).delete()
        firebaseRepository.getUser().delete()

        firebaseUser.delete().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                loginPrefsEditor.clear().commit()
                DialogUtils.launchLogInFragment(fm)
                isProgressBarLoasing.value = false
            }
        }
    }
}