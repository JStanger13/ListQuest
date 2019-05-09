package com.example.listquest.ui.dialogs

import androidx.fragment.app.DialogFragment
import android.view.WindowManager
import android.widget.EditText
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.custom_edit_dialog.*
import com.example.listquest.MainActivity
import com.example.listquest.R
import com.example.listquest.models.MainQuestModel
import com.example.listquest.ui.mainquest.MainQuestFragment
import kotlinx.android.synthetic.main.fragment_main_quest.*


class EditDialogFragment: DialogFragment(){
    private var mEditText: EditText? = null
    lateinit var mainQuest: MainQuestModel
    //var position = 0

    override fun getTheme(): Int {
        return com.example.listquest.R.style.AppAlertTheme
    }

    fun newInstance(position: Int): EditDialogFragment {
        val frag = EditDialogFragment()
        val args = Bundle()
        args.putInt("title", position)
        //this.position = position
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
        return inflater.inflate(com.example.listquest.R.layout.custom_edit_dialog, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get field from view
        mEditText = view.findViewById(com.example.listquest.R.id.body)
        // Fetch arguments from bundle and set title
        var position = arguments!!.getInt("title", 0)
        dialog.setTitle(MainActivity.mainFrag.getQuestList()[position].mainQuestTitle)
        mEditText!!.setText(MainActivity.mainFrag.getQuestList()[position].mainQuestTitle)
        // Show soft keyboard automatically and request focus to field
        mEditText!!.requestFocus()
        dialog.window!!.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
        )
        yesBtn.setOnClickListener{
            MainActivity.mainFrag.getQuestList()[position].mainQuestTitle = mEditText!!.text.toString()
            MainActivity.mainFrag.mainAdapter.notifyDataSetChanged()
            //MainActivity.mainFrag.recycler_view.adapter = MainActivity.mainFrag.mainAdapter
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