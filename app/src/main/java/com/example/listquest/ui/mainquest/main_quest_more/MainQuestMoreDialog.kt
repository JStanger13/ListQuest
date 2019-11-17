package com.example.listquest.ui.mainquest.main_quest_more

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
import com.example.listquest.databinding.DialogMainQuestMoreBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.custom_create_mainquest.*


class MainQuestMoreDialog(): BottomSheetDialogFragment() {
    private lateinit var viewModel: MainQuestMoreDialogViewModel
    lateinit var binding: DialogMainQuestMoreBinding



    override fun getTheme(): Int {
        return R.style.AppAlertTheme
    }

    fun newInstance(): MainQuestMoreDialog {
        val frag = MainQuestMoreDialog()
//        val args = Bundle()
//        frag.arguments = args
        return frag
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(arg0: Bundle?) {
        super.onActivityCreated(arg0)

        setUpViewModel()

        dialog.window!!.attributes.windowAnimations = R.style.DialogSlideAnim
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

    private fun setUpViewModel(){
        viewModel = ViewModelProviders.of(this,
            MainQuestMoreDialogViewModelFactory(
                fragmentManager!!,
                dialog!!)
        ).get(MainQuestMoreDialogViewModel::class.java)
        binding.setVariable(BR.viewModel, viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_main_quest_more, container, false)
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