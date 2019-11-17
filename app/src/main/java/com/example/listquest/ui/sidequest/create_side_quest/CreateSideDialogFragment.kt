package com.example.listquest.ui.sidequest.create_side_quest

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.example.listquest.BR
import com.example.listquest.R
import com.example.listquest.data.utils.KeyboardUtils
import com.example.listquest.databinding.CustomCreateSidequestBinding

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.custom_create_sidequest.*
import kotlinx.android.synthetic.main.custom_create_sidequest.view.*

class CreateSideDialogFragment(private var callback: CreateSideQuestListener): BottomSheetDialogFragment() {
    private lateinit var viewModel: CreateSideDialogViewModel
    lateinit var binding: CustomCreateSidequestBinding

    interface CreateSideQuestListener {
        fun addSideQuestFromDialog(id: String, newSideQuestTitle: String)
    }

    override fun getTheme(): Int {
        return R.style.AppAlertTheme
    }

    fun newInstance(id: String): CreateSideDialogFragment {
        val frag = CreateSideDialogFragment(callback)
        val args = Bundle()
        args.putString("SideQuestId", id)
        frag.arguments = args
        return frag
    }

    override fun onActivityCreated(arg0: Bundle?) {
        super.onActivityCreated(arg0)

        setUpViewModel()

        KeyboardUtils.showKeyboard(create_edit_text, context!!)

        dialog.window!!
            .attributes.windowAnimations = R.style.DialogSlideAnim
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.custom_create_sidequest, container, false)
        binding.lifecycleOwner = this
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        view.send_side.setOnClickListener {
            callback.addSideQuestFromDialog(
                arguments!!.getString("SideQuestId")!!,
                create_edit_text.text.toString()
            )
            view.create_edit_text.setText("")
            dialog.dismiss()
        }
    }

    override fun onResume() {
        val params = dialog.window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window!!.attributes = params as WindowManager.LayoutParams
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()

        var view = activity!!.currentFocus
        if (view == null) view = View(activity)
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            ?: return
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            CreateSideDialogFragmentViewModelFactory(
                arguments!!.getString("SideQuestId")!!,
                callback,
                dialog
            )
        ).get(CreateSideDialogViewModel::class.java)
        binding.setVariable(BR.viewModel, viewModel)
    }
}