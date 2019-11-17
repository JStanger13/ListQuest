package com.example.listquest.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.example.listquest.BR
import com.example.listquest.R
import com.example.listquest.databinding.DialogEditNotesBinding
import com.example.listquest.data.models.NotesModel


class NotesDialogFragment(private var callback: NotesListener): DialogFragment() {
    private lateinit var viewModel: NotesDialogFragmentViewModel
    lateinit var binding: DialogEditNotesBinding


    interface NotesListener {
        fun createNotesDialog(notes: NotesModel)
        fun editNotesDialog(id: String, title: String, body: String)
    }

    override fun getTheme(): Int {
        return R.style.AppAlertTheme
    }

    fun newInstance(id: String,
                    title: String,
                    body: String): NotesDialogFragment {
        val frag = NotesDialogFragment(callback)
        val args = Bundle()
        args.putString("id", id)
        args.putString("title", title)
        args.putString("body", body)

        frag.arguments = args

        return frag
    }

    override fun onActivityCreated(arg0: Bundle?) {
        super.onActivityCreated(arg0)
        setUpViewModel()
        dialog.window!!
            .attributes.windowAnimations = R.style.DialogSlideAnim
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            NotesDialogFragmentViewModelFactory(
                callback,
                arguments!!.getString("id")!!,
                dialog,
                arguments!!.getString("title")!!,
                arguments!!.getString("body")!!
            )

        ).get(NotesDialogFragmentViewModel::class.java)
        binding.setVariable(BR.viewModel, viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_edit_notes, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //KeyboardUtils.showKeyboard(edit_notes_body_text, activity!!)
    }

    override fun onResume() {
        val params = dialog.window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = params as WindowManager.LayoutParams
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()

        var view = activity!!.currentFocus
        if (view == null) view = View(activity)
//        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            ?: return
//        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}