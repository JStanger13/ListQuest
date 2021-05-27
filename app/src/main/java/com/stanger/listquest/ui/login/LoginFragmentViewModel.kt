package com.stanger.listquest.ui.login


import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.stanger.listquest.data.models.UserModel
import com.stanger.listquest.ui.forgot_password.ForgotPasswordFragment
import com.stanger.listquest.ui.sign_up.SignUpFragment
import com.stanger.listquest.utils.DialogUtils
import com.stanger.listquest.utils.ErrorListener

class LoginFragmentViewModel(
    val fm: FragmentManager,
    val context: Context,
    val callback: ErrorListener,
    val loginPreferences: SharedPreferences
) : ViewModel(){
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val errorEmail = MutableLiveData<String>()
    val errorPassword = MutableLiveData<String>()
    val isProgressBarLoasing = MutableLiveData<Boolean>()
    val isRememberMe = MutableLiveData<Boolean>()
    val loginPrefsEditor = loginPreferences.edit()
    val saveLogin = loginPreferences.getBoolean("saveLogin", false)

    init{
        isProgressBarLoasing.value = false
        setFieldsRememberMe()
    }

    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance()}

    private val currentUserDocRef: DocumentReference
        get() = firestoreInstance.document("Users/${ FirebaseAuth.getInstance().uid
            ?: throw NullPointerException("UID is null.")}")

    fun createAccount(){
        DialogUtils.launchCreateAccountFragment(fm, SignUpFragment())
    }

    fun launchForgotPasswordViewModel(){
        DialogUtils.launchForgotPasswordFragment(fm, ForgotPasswordFragment())
    }

    fun logIn() {
        isProgressBarLoasing.value = true
        if(!email.value.isNullOrEmpty() && !password.value.isNullOrEmpty()) {
            saveFieldsRememberMe()
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email.value!!, password.value!!)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
                            if (documentSnapshot.exists()) {
                                getUser {
                                    isProgressBarLoasing.value = false
                                }
                            }
                        }
                    } else {
                        errorEmail.value = "ERROR EMAIL"
                        errorPassword.value = "ERROR PASSWORD"
                        Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show()
                        isProgressBarLoasing.value = false
                    }
                }
        } else {
            if(email.value.isNullOrEmpty()) errorEmail.value = "please type your email"
            if(password.value.isNullOrEmpty()) errorEmail.value = "please type your password"
            isProgressBarLoasing.value = false
        }
    }
    private fun getUser(onComplete: () -> Unit){
        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                DialogUtils.launchMainQuestFromLogin(fm, documentSnapshot.toObject(UserModel::class.java)!!)
                onComplete()
            } else{
                Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setFieldsRememberMe(){
        if (saveLogin) {
            email.value = loginPreferences.getString("username", "")
            password.value = loginPreferences.getString("password", "")
            isRememberMe.value = true
        } else isRememberMe.value = false
    }

    private fun saveFieldsRememberMe(){
        if (isRememberMe.value!!) {
            loginPrefsEditor.putBoolean("saveLogin", true)
            loginPrefsEditor.putString("username", email.value)
            loginPrefsEditor.putString("password", password.value)
            loginPrefsEditor.commit()
        } else {
            loginPrefsEditor.clear()
            loginPrefsEditor.commit()
        }
    }
}