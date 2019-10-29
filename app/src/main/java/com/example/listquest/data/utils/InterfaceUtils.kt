package com.example.listquest.utils

import com.example.listquest.ui.dialogs.CreateSideDialogFragment

interface CreateSideQuestListener {
    fun addSideQuestFromDialog(id: String, newSideQuestTitle: String)
}

interface LaunchSideQuestListener {
    fun launchSideQuestDialog(createSideDialogFragment: CreateSideDialogFragment)
}

interface OnBackPressedListener {
    fun onBackPressed()
}