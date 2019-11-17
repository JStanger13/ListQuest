package com.example.listquest.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.listquest.R
import kotlinx.android.synthetic.main.custom_side_edit_dialog.*

class EditSideDialogFragment: DialogFragment(){

    private var mEditText: EditText? = null

    override fun getTheme(): Int {
        return R.style.AppAlertTheme
    }

    fun newInstance(key: String, title: String): EditSideDialogFragment {
        val frag = EditSideDialogFragment()
        val args = Bundle()
        args.putString("title", title)
        args.putString("key", key)
        frag.arguments = args

        return frag
    }

    override fun onActivityCreated(arg0: Bundle?) {
        super.onActivityCreated(arg0)
        dialog.window!!
            .attributes.windowAnimations = R.style.DialogSlideAnim
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.custom_side_edit_dialog, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get field from view
        mEditText = view.findViewById(com.example.listquest.R.id.create_edit_text)
        mEditText!!.setText(arguments!!.getString("title"))

        // Show soft keyboard automatically and request focus to field
        mEditText!!.requestFocus()
        dialog.window!!.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
        )

        yes_side_btn.setOnClickListener{


//            mainQuestRef.document(arguments!!.getString("SideQuestId")!!)
//                .collection("SideQuestCollection")
//                .document(arguments!!.getString("key")!!)
//                .set()

            dialog.dismiss()
        }

        no_side_btn.setOnClickListener{
            dialog.dismiss()
        }
    }

    override fun onResume() {
        // Get existing layout params for the window
        val params = dialog.window!!.attributes
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = params as android.view.WindowManager.LayoutParams
        // Call super onResume after sizing
        super.onResume()
    }
}