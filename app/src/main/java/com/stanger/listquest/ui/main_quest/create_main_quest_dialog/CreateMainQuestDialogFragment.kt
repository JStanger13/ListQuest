package com.stanger.listquest.ui.main_quest.create_main_quest_dialog


import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.stanger.listquest.BR
import com.stanger.listquest.R
import com.stanger.listquest.databinding.DialogCreateMainQuestBinding
import com.stanger.listquest.utils.CreateQuestListener
import com.stanger.listquest.utils.KeyboardUtils
import kotlinx.android.synthetic.main.dialog_create_main_quest.*


class CreateMainQuestDialogFragment(
    private var callback: CreateQuestListener,
    private var bossInt: Int,
    private val dateTxt: String,
    private val timeTxt: String,
    private val oldName: String
): BottomSheetDialogFragment() {
    private lateinit var viewModel: CreateMainQuestDialogViewModel
    lateinit var binding: DialogCreateMainQuestBinding


//    override fun getTheme(): Int {
//        return R.style.AppAlertTheme
//    }

    fun newInstance(): CreateMainQuestDialogFragment {
        val frag = CreateMainQuestDialogFragment(callback, bossInt, dateTxt, timeTxt, oldName)
        val args = Bundle()
        frag.arguments = args
        return frag
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(arg0: Bundle?) {
        super.onActivityCreated(arg0)

        setUpViewModel()
        KeyboardUtils.showKeyboard(edit_text_name, context!!)

        dialog!!.window!!.attributes.windowAnimations = R.style.DialogSlideAnim
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

    private fun setUpViewModel(){
        viewModel = ViewModelProviders.of(this,
            CreateMainQuestDialogViewModelFactory(
                callback,
                context!!,
                bossInt,
                resources,
                activity!!.supportFragmentManager,
                dialog!!,
                dateTxt,
                timeTxt,
                oldName)
        ).get(CreateMainQuestDialogViewModel::class.java)
        binding.setVariable(BR.viewModel, viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_create_main_quest, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onResume() {
        val params = dialog!!.window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
        binding.editTextName.setSelection(oldName.length)

        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()

        var view = activity!!.currentFocus
        if (view == null) view = View(activity)
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}