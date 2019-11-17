package com.example.listquest.ui.mainquest.create_main_quest


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
import com.example.listquest.BR
import com.example.listquest.R
import com.example.listquest.data.utils.KeyboardUtils
import com.example.listquest.databinding.CustomCreateMainquestBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.custom_create_mainquest.*


class CreateMainDialogFragment(
    private var callback: CreateQuestListener,
    private var bossInt: Int,
    private val dateTxt: String,
    private val timeTxt: String
): BottomSheetDialogFragment() {
    private lateinit var viewModel: CreateMainDialogFragmentViewModel
    lateinit var binding: CustomCreateMainquestBinding


    interface CreateQuestListener {
        fun addMainQuestFromDialog(newMainQuestTitle: String, bossImg: Int, bossName: String, dateText: String, timeText: String)
    }

    override fun getTheme(): Int {
        return R.style.AppAlertTheme
    }

    fun newInstance(): CreateMainDialogFragment {
        val frag = CreateMainDialogFragment(callback, bossInt, dateTxt, timeTxt)
        val args = Bundle()
        frag.arguments = args
        return frag
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(arg0: Bundle?) {
        super.onActivityCreated(arg0)

        setUpViewModel()
        KeyboardUtils.showKeyboard(edit_text_name, context!!)

        dialog.window!!.attributes.windowAnimations = R.style.DialogSlideAnim
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

    private fun setUpViewModel(){
        viewModel = ViewModelProviders.of(this,
            CreateMainDialogFragmentViewModelFactory(
                callback,
                context!!,
                bossInt,
                resources,
                activity!!.supportFragmentManager,
                dialog,
                dateTxt,
                timeTxt)
        ).get(CreateMainDialogFragmentViewModel::class.java)
        binding.setVariable(BR.viewModel, viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.custom_create_mainquest, container, false)
        binding.lifecycleOwner = this
        return binding.root
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
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}