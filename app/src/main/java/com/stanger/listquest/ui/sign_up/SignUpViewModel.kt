package com.stanger.listquest.ui.sign_up


import android.content.Context
import android.content.SharedPreferences
import android.util.Patterns
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.stanger.listquest.data.models.UserModel
import com.stanger.listquest.ui.login.LoginFragment
import com.stanger.listquest.utils.DialogUtils


class SignUpViewModel(val fm: FragmentManager,
                      val context: Context,
                      val loginPreferences: SharedPreferences
) : ViewModel() {
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val password2 = MutableLiveData<String>()
    val errorEmail = MutableLiveData<String>()
    val errorPassword = MutableLiveData<String>()
    val isProgressBarLoasing = MutableLiveData<Boolean>()
    val isRememberMe = MutableLiveData<Boolean>()
    val loginPrefsEditor = loginPreferences.edit()

    init {
        isRememberMe.value = false
    }

    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance()}

    private val currentUserDocRef: DocumentReference
        get() = firestoreInstance.document("Users/${ FirebaseAuth.getInstance().uid
            ?: throw NullPointerException("UID is null.")}")

    fun login() = DialogUtils.launchLoginFragment(fm, LoginFragment())

    private fun String.isValidEmail(): Boolean
            = this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    private fun validatePassword(): Boolean {
        return password.value == password2.value
    }

    fun createUser() {
        isProgressBarLoasing.value = true
        if(validatePassword() && email.value!!.isValidEmail()){
            saveFieldsRememberMe()
            if (!email.value.isNullOrEmpty() &&  !password.value.isNullOrEmpty()) {
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email.value!!, password.value!!)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            initCurrentUserIfFirstTime {

                            }
                        } else {
                            isProgressBarLoasing.value = false
                            errorEmail.value = "ERROR EMAIL"
                            errorPassword.value = "ERROR PASSWORD"
                        }
                    }
            } else{
                isProgressBarLoasing.value = false
                if(email.value.isNullOrEmpty()) errorEmail.value = "please type your email"
                if(password.value.isNullOrEmpty()) errorEmail.value = "please type your password"
            }
        }else{
            Toast.makeText(context, "Passwords Do Not Match",  Toast.LENGTH_SHORT).show()
            isProgressBarLoasing.value = false
        }
    }

    private fun initCurrentUserIfFirstTime(onComplete: () -> Unit) {
        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                val user = FirebaseAuth.getInstance().currentUser
                val newUser = UserModel(
                    user!!.uid,
                    "name",
                    " ",
                    1,
                    1,
                    0,
                    0,
                    0,
                    false,
                    true,
                    "",
                    "",
                    email.value!!
                )
                currentUserDocRef.set(newUser).addOnSuccessListener {
                    DialogUtils.launchMainQuestFromLogin(fm, newUser)

                    onComplete()
                    isProgressBarLoasing.value = false
                }
            } else {
                DialogUtils.launchMainQuestFromLogin(fm, documentSnapshot.toObject(UserModel::class.java)!!)
                onComplete()
                isProgressBarLoasing.value = false
            }
        }
    }

    private fun saveFieldsRememberMe(){
        if (isRememberMe.value!!) {
            loginPrefsEditor.putBoolean("saveLogin", true)
            loginPrefsEditor.putString("username", email.value)
            loginPrefsEditor.putString("password", password.value)
            loginPrefsEditor.commit();
        } else {
            loginPrefsEditor.clear()
            loginPrefsEditor.commit()
        }
    }
}