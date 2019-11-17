package com.example.listquest.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.listquest.BR
import com.example.listquest.R
import com.example.listquest.databinding.CustomEditNotesDialogBinding
import com.example.listquest.data.models.MainQuestModel
import com.example.listquest.data.models.NotesModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditNotesDialogFragment(private var callback: EditNotesListener): BottomSheetDialogFragment(){
    private lateinit var viewModel: EditNotesDialogViewModel
    lateinit var binding: CustomEditNotesDialogBinding

    interface EditNotesListener {
            fun editNotesFromDialog(id: String, noteId: String)
    }



        fun newInstance(notes: NotesModel, mainQuest: MainQuestModel): EditNotesDialogFragment {
            val frag = EditNotesDialogFragment(callback)
            val args = Bundle()
            args.putSerializable("notes", notes)
            args.putSerializable("mainQuest", mainQuest)
            frag.arguments = args

            return frag
        }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpViewModel()

        dialog.window!!
            .attributes.windowAnimations = R.style.DialogSlideAnim
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }

    override fun getTheme(): Int {
        return R.style.AppAlertTheme
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.custom_edit_notes_dialog, container, false)
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

    private fun setUpViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            EditNotesDialogViewModelFactory(
                arguments!!.getSerializable("notes") as NotesModel,
                fragmentManager!!,
                dialog,
                arguments!!.getSerializable("mainQuest") as MainQuestModel,
                callback)
        ).get(EditNotesDialogViewModel::class.java)
        binding.setVariable(BR.viewModel, viewModel)
    }
}