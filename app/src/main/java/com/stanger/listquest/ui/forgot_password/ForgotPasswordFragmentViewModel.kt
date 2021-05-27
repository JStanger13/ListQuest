package com.stanger.listquest.ui.forgot_password


import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.stanger.listquest.ui.side_quest.SideQuestViewModel

class ForgotPasswordFragmentViewModel(val context: Context,
                                      val fm: FragmentManager) : ViewModel(){
    val email = MutableLiveData<String>()
    val errorEmail = MutableLiveData<String>()
    val isProgressBarLoasing = MutableLiveData<Boolean>()

    fun doneClicked(){
        fm.popBackStack()
    }

    fun sendEmail(){
        isProgressBarLoasing.value = true
        val auth = FirebaseAuth.getInstance()
        val emailAddress = email.value!!

        auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Email sent.", Toast.LENGTH_SHORT).show()
                    isProgressBarLoasing.value = false

                    Log.d(SideQuestViewModel.TAG, "Email sent.")
                }
                else {
                    Toast.makeText(context, "Make sure you have the correct email.", Toast.LENGTH_SHORT).show()
                    isProgressBarLoasing.value = false
                }
            }
    }
}