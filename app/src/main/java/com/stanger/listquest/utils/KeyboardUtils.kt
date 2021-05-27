package com.stanger.listquest.utils


import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.stanger.listquest.ui.main_quest.create_main_quest_dialog.CreateMainQuestDialogFragment

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


        fun detectKeyboardOpen(rootView: View, dialog: CreateMainQuestDialogFragment) {
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