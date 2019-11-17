package com.example.listquest.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.listquest.R
import com.example.listquest.data.models.MainQuestModel
import kotlinx.android.synthetic.main.custom_edit_dialog.*

class EditDialogFragment(private var callback: EditQuestListener): DialogFragment(){

    private var mEditText: EditText? = null

    interface EditQuestListener {
        fun editQuestFromDialog(mainQuestModel: MainQuestModel)
    }

    fun newInstance(mainQuestModel: MainQuestModel): EditDialogFragment {
        val frag = EditDialogFragment(callback)
        val args = Bundle()
        args.putSerializable("mainQuest", mainQuestModel)
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
        return inflater.inflate(R.layout.custom_edit_dialog, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get field from view
        val mainQuest = arguments!!.getSerializable("mainQuest") as MainQuestModel
        mEditText = view.findViewById(R.id.body)
        mEditText!!.setText(mainQuest.mainQuestTitle)


        // Show soft keyboard automatically and request focus to field
        mEditText!!.requestFocus()
        dialog.window!!.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
        )

        yesBtn.setOnClickListener{
            mainQuest.mainQuestTitle = mEditText!!.text.toString()
            callback.editQuestFromDialog(mainQuest)
            dialog.dismiss()
        }

        noBtn.setOnClickListener{
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