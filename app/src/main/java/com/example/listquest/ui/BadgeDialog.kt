package com.example.listquest.ui

import android.content.DialogInterface
import android.R
import android.app.Dialog
import android.view.LayoutInflater
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment


class BadgeDialog : AppCompatDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(this.activity!!)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.simple_dropdown_item_1line, null)
        builder.setView(view)
            .setTitle("Login")
            .setNegativeButton("cancel", DialogInterface.OnClickListener { dialogInterface, i -> })
        return builder.create()
    }
}