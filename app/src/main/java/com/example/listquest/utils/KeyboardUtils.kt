package com.example.listquest.utils

import android.content.Context
import android.graphics.Rect
import android.opengl.Visibility
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class KeyboardUtils {
    companion object {
        fun showSoftKeyboard(view: View, imm: InputMethodManager) {
            if (view.requestFocus()) {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }
        }
        fun hideSoftKeyBoard(context: Context, view: View) {
            try {
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun detectKeyboardOpen(rootView: View, rv: RecyclerView) {
            rootView.viewTreeObserver.addOnGlobalLayoutListener {
                val r = Rect()
                rootView.getWindowVisibleDisplayFrame(r)

                val heightDiff = rootView.rootView.height - (r.bottom - r.top)
                if (heightDiff > rootView.rootView.height / 4) {
                    rv.visibility = View.GONE
                } else {
                    rv.visibility = View.VISIBLE
                }
            }
        }
    }
}