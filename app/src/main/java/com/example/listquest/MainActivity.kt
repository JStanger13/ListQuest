package com.example.listquest

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.listquest.ui.mainquest.MainQuestFragment
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.main_activity.*
import java.util.*

class MainActivity : AppCompatActivity() {
    val TAG = "LOGIN_ACTIVITY"
    lateinit var providers: List<AuthUI.IdpConfig>
    private val MY_REQUEST_CODE: Int = 7117

    companion object {
        lateinit var mainFrag: MainQuestFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        loading_view.show()
        if (savedInstanceState == null) {
            mainFrag = MainQuestFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .add(R.id.container, MainQuestFragment.newInstance(), "")
                .commitNow()
            providers = Arrays.asList<AuthUI.IdpConfig>(
                AuthUI.IdpConfig.EmailBuilder().build(), //EmailLogin
                AuthUI.IdpConfig.FacebookBuilder().build(),//Facebook Login
                AuthUI.IdpConfig.GoogleBuilder().build(), //google login
                AuthUI.IdpConfig.PhoneBuilder().build()//PhoneLogin
            )
            showSignInOptions()
        }
    }

    private fun showSignInOptions() {
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.AppTheme)
            .setLogo(R.drawable.king)
            .build(), MY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_REQUEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                Toast.makeText(this, "" + user!!.email, Toast.LENGTH_SHORT).show()
                loading_view.hide()
                container.visibility = View.VISIBLE
            } else {
                Toast.makeText(this, "" + response!!.error!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

