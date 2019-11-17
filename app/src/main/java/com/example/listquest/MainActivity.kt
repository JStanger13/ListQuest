package com.example.listquest

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.listquest.data.utils.DialogUtils
import com.example.listquest.ui.login.LoginFragment
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : AppCompatActivity() {

    companion object {
        var main = MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        DialogUtils.imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        container.visibility = View.VISIBLE
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left)
                .add(R.id.container, LoginFragment.newInstance(), "")
                .commitNow()
        }
    }
}
