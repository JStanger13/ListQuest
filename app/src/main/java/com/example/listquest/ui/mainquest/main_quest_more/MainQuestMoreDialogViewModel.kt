package com.example.listquest.ui.mainquest.main_quest_more

import android.app.Dialog
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import com.example.listquest.R
import com.example.listquest.ui.login.LoginFragment
import com.google.firebase.auth.FirebaseAuth

class MainQuestMoreDialogViewModel(
    val fm: FragmentManager,
    val dialog: Dialog
): ViewModel() {

    fun logOut(){
        FirebaseAuth.getInstance().signOut()
        fm.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left)
            .add(R.id.container, LoginFragment.newInstance(), "")
            .commitNow()
    }
}