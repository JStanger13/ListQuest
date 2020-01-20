package com.example.listquest.ui.profile_fragment.delete_dialog

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.listquest.BR
import com.example.listquest.R
import com.example.listquest.data.models.UserModel
import com.example.listquest.databinding.DialogDeleteBinding

class DeleteDialog(var userModel: UserModel): Fragment() {
    private lateinit var viewModel: DeleteDialogViewModel
    lateinit var binding: DialogDeleteBinding


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(arg0: Bundle?) {
        super.onActivityCreated(arg0)

        setUpViewModel()
    }

    private fun setUpViewModel(){
        viewModel = ViewModelProviders.of(this,
            DeleteDialogViewModelFactory(fragmentManager!!, userModel, context!!)
        ).get(DeleteDialogViewModel::class.java)

        binding.setVariable(BR.viewModel, viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_delete, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        var view = activity!!.currentFocus
        if (view == null) view = View(activity)
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}