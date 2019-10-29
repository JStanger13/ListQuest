package com.example.listquest.utils

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager


class BaseBackPressedListener(private val activity: FragmentActivity) : OnBackPressedListener{

    override fun onBackPressed() {
        activity.getSupportFragmentManager()
            .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}