package com.stanger.listquest.ui.side_quest.create_side_quest_dialog


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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.stanger.listquest.BR
import com.stanger.listquest.R
import com.stanger.listquest.databinding.DialogCreateSideQuestBinding
import com.stanger.listquest.utils.CreateSideQuestListener
import com.stanger.listquest.utils.KeyboardUtils
import kotlinx.android.synthetic.main.dialog_create_side_quest.*
import kotlinx.android.synthetic.main.dialog_create_side_quest.view.*

class CreateSideQuestDialogFragment(private var callback: CreateSideQuestListener): BottomSheetDialogFragment() {
    private lateinit var viewModel: CreateSideQuestDialogViewModel
    lateinit var binding: DialogCreateSideQuestBinding

//    override fun getTheme(): Int {
//        return R.style.AppAlertTheme
//    }

    fun newInstance(id: String): CreateSideQuestDialogFragment {
        val frag = CreateSideQuestDialogFragment(callback)
        val args = Bundle()
        args.putString("SideQuestId", id)
        frag.arguments = args
        return frag
    }

    override fun onActivityCreated(arg0: Bundle?) {
        super.onActivityCreated(arg0)

        setUpViewModel()

        KeyboardUtils.showKeyboard(create_edit_text, context!!)

        dialog!!.window!!
            .attributes.windowAnimations = R.style.DialogSlideAnim
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_create_side_quest, container, false)
        binding.lifecycleOwner = this
        dialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
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
            dialog!!.dismiss()
        }
    }

    override fun onResume() {
        val params = dialog!!.window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
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
            CreateSideQuestDialogViewModelFactory(
                arguments!!.getString("SideQuestId")!!,
                callback,
                dialog!!
            )
        ).get(CreateSideQuestDialogViewModel::class.java)
        binding.setVariable(BR.viewModel, viewModel)
    }
}