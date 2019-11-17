package com.example.listquest.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.listquest.R
import com.example.listquest.ui.mainquest.MainQuestFragment
import com.example.listquest.data.utils.FirestoreUtil
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.*


class LoginFragment : Fragment() {

    lateinit var providers: List<AuthUI.IdpConfig>
    private val MY_REQUEST_CODE: Int = 7117

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_login, container, false)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        providers = Arrays.asList<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.EmailBuilder().build(), //EmailLogin
            AuthUI.IdpConfig.FacebookBuilder().build(), //Facebook Login
            AuthUI.IdpConfig.GoogleBuilder().build(), //google login
            AuthUI.IdpConfig.PhoneBuilder().build() //PhoneLogin
        )
        showSignInOptions()
    }

    private fun showSignInOptions() {
        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.AppTheme)
                .setLogo(R.drawable.list_quest_logo)
                .build(), MY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == MY_REQUEST_CODE) {

            val response = IdpResponse.fromResultIntent(data)

            when (resultCode) {
                Activity.RESULT_OK -> FirestoreUtil.initCurrentUserIfFirstTime {
                    switchFrag()
                    login_progress.hide()
                }
                Activity.RESULT_CANCELED -> {
                    if(response == null) return

                    when(response.error?.errorCode){
                        ErrorCodes.NO_NETWORK ->
                            Toast.makeText(context, "No Network", Toast.LENGTH_LONG).show()
                        ErrorCodes.UNKNOWN_ERROR ->
                            Toast.makeText(context, "Unknown error", Toast.LENGTH_LONG).show()
                    }

                }
                else -> Toast.makeText(activity, "" + response!!.error!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun switchFrag(){
        activity!!.supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left)
            .replace(R.id.container, MainQuestFragment.newInstance(), "")
            .commit()
    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}