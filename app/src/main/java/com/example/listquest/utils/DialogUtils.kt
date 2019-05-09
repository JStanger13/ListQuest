package com.example.listquest.utils

import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog

class DialogUtils{
    companion object {
        fun createDialog(view: View) {
            val dialogBuilder = AlertDialog.Builder(view.context)

            // set message of alert dialog
            dialogBuilder.setMessage("Do you want to close this application ?")
                // if the dialog is cancelable
                .setCancelable(false)
                // positive button text and action
                .setPositiveButton("Proceed", DialogInterface.OnClickListener { dialog, id ->
                    dialog.dismiss()
                })
                // negative button text and action
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                })

            // create dialog box
            val alert = dialogBuilder.create()
            // set title for alert dialog box
            alert.setTitle("AlertDialogExample")
            // show alert dialog
            alert.show()
        }
    }
}