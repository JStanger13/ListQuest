package com.stanger.listquest.ui.side_quest.edit_side_quest_dialog


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
import com.stanger.listquest.data.models.SideQuestModel
import com.stanger.listquest.databinding.DialogEditSideQuestBinding
import com.stanger.listquest.utils.KeyboardUtils
import com.stanger.listquest.utils.SideEditQuestListener
import kotlinx.android.synthetic.main.dialog_edit_side_quest.*


class EditSideQuestDialogFragment(val callback: SideEditQuestListener): BottomSheetDialogFragment(){
    private lateinit var viewModel: EditSideQuestDialogViewModel
    lateinit var binding: DialogEditSideQuestBinding

//    override fun getTheme(): Int {
//        return R.style.AppAlertTheme
//    }

    fun newInstance(sideQuest: SideQuestModel): EditSideQuestDialogFragment {
        val frag = EditSideQuestDialogFragment(callback)
        val args = Bundle()
        args.putSerializable("sideQuest", sideQuest)
        frag.arguments = args
        return frag
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(arg0: Bundle?) {
        super.onActivityCreated(arg0)

        setUpViewModel()
        KeyboardUtils.showKeyboard(edit_side_edit, context!!)

        dialog!!.window!!.attributes.windowAnimations = R.style.DialogSlideAnim
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

    }

    private fun setUpViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            EditSideQuestDialogViewModelFactory(
                arguments!!.getSerializable("sideQuest")!! as SideQuestModel,
                dialog!!,
                callback)
        ).get(EditSideQuestDialogViewModel::class.java)
        binding.setVariable(BR.viewModel, viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_edit_side_quest, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onResume() {
        val params = dialog!!.window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams

        binding.editSideEdit.setSelection((arguments!!.getSerializable("sideQuest")!! as SideQuestModel).sideQuestTitle.length)

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