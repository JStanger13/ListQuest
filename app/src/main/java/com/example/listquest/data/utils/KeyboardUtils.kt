package com.example.listquest.data.utils

import android.content.Context
import android.graphics.Rect
import android.text.InputType
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.example.listquest.ui.mainquest.create_main_quest.CreateMainDialogFragment

class KeyboardUtils {
    companion object {
        fun showKeyboard(editText: EditText, context: Context) {
            editText.post {
                editText.requestFocusFromTouch()

                val lManager =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                lManager.showSoftInput(editText,  InputMethodManager.SHOW_IMPLICIT)
            }
        }

        fun hideSoftKeyBoard(view: View) {
            val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }


        fun detectKeyboardOpen(rootView: View, dialog: CreateMainDialogFragment) {
            rootView.viewTreeObserver.addOnGlobalLayoutListener {
                val r = Rect()
                rootView.getWindowVisibleDisplayFrame(r)

                val heightDiff = rootView.rootView.height - (r.bottom - r.top)
                if (heightDiff > rootView.rootView.height / 4) {
                    dialog.dismiss()
                } else {}
            }
        }
    }
}