package com.example.listquest.ui.profile_fragment.delete_dialog

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.listquest.MainActivity
import com.example.listquest.R
import com.example.listquest.data.models.UserModel
import com.example.listquest.data.utils.DialogUtils
import com.example.listquest.data.utils.FirestoreRepository
import com.example.listquest.ui.login.forgot_password.ForgotPasswordFragment
import com.example.listquest.ui.sidequest.SideQuestViewModel.Companion.TAG
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class DeleteDialogViewModel(val fm: FragmentManager,
                            val userModel: UserModel,
                            val context: Context
): ViewModel() {
    var firebaseRepository = FirestoreRepository()

    val user = FirebaseAuth.getInstance().currentUser

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    init {
        email.value = ""
        password.value = ""
    }

    fun doneClicked() {
        fm.popBackStack()
    }

    fun deleteUser() {
        //check credentials AND edit texts!!
        if(!email.value.isNullOrEmpty() &&  !password.value.isNullOrEmpty()) {

            val credential = EmailAuthProvider.getCredential(email.value!!, password.value!!)

            user?.reauthenticate(credential)
                ?.addOnCompleteListener {
                    //KeyboardUtils.hideSoftKeyBoard(dialog.findViewById(R.id.edit_text_email))
                    if (it.isSuccessful) {
                        Log.d(TAG, "User re-authenticated.")
                        deleteAllFields(user)
                    } else {
                        Toast.makeText(
                            context,
                            "Make sure you have the correct email.",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d(TAG, "ERROR  User re-authenticated.")
                    }
                }
        }
    }

    fun forgotPassword(){
        fm.popBackStack()
        DialogUtils.launchForgotPasswordFragment(fm, ForgotPasswordFragment())
    }


    private fun deleteAllFields(firebaseUser: FirebaseUser) {
        firebaseRepository.deleteSnapMain()
        firebaseRepository.deleteSnapMainFavorite()
        firebaseRepository.deleteSnapMainCompleted()
        firebaseRepository.deleteSnapImagesCompleted()
        firebaseRepository.storageRef.child("photos").child(userModel.profilePicturePath).delete()
        firebaseRepository.getUser().delete()

        firebaseUser.delete().addOnCompleteListener { task ->
            if (task.isSuccessful) {

                fm.beginTransaction()
                    .replace(R.id.container, MainActivity.loginFragment, "")
                    .commitNow()
            }
        }
    }
}