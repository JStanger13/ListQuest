//package com.example.listquest.utils
//
//import android.app.Activity
//import android.content.Intent
//import android.widget.Toast
//import com.example.listquest.R
//import com.firebase.ui.auth.AuthUI
//import com.firebase.ui.auth.IdpResponse
//import com.google.firebase.auth.FirebaseAuth
//import kotlinx.android.synthetic.main.activity_login.*
//import java.util.*
//
//class LoginUtils {
//    companion object {
//        val TAG = "LOGIN_ACTIVITY"
//        lateinit var providers: List<AuthUI.IdpConfig>
//        private val MY_REQUEST_CODE: Int = 7117
//
//
//        fun provideProviders(){
//            providers = Arrays.asList<AuthUI.IdpConfig>(
//                AuthUI.IdpConfig.EmailBuilder().build(), //EmailLogin
//                AuthUI.IdpConfig.FacebookBuilder().build(),//Facebook Login
//                AuthUI.IdpConfig.GoogleBuilder().build(), //google login
//                AuthUI.IdpConfig.PhoneBuilder().build()//PhoneLogin
//            )
//        }
//        fun getResult(requestCode: Int, resultCode: Int, data: Intent?){
//            if (requestCode == MY_REQUEST_CODE) {
//                val response = IdpResponse.fromResultIntent(data)
//                if (resultCode == Activity.RESULT_OK) {
//                    val user = FirebaseAuth.getInstance().currentUser
//                    Toast.makeText(this, "" + user!!.email, Toast.LENGTH_SHORT).show()
//
//                    //btn_sign_out.isEnabled = true
//                } else {
//                    Toast.makeText(this, "" + response!!.error!!.message, Toast.LENGTH_SHORT).show()
//                }
//
//            }
//        }
//
//    }
//}