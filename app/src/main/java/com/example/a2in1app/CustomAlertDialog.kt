package com.example.a2in1app

import android.app.Activity
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

class CustomAlertDialog(activity: Activity, message: String) {
    init {
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(activity)

        // set message of alert dialog
        dialogBuilder.setMessage(message)
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Yes", DialogInterface.OnClickListener {
                    dialog, id -> activity.recreate()
            })
            // negative button text and action
            .setNegativeButton("No", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Game Over")
        // show alert dialog
        alert.show()
    }
}