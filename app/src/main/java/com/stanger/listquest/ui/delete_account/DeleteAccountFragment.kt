package com.stanger.listquest.ui.delete_account


import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.stanger.listquest.BR
import com.stanger.listquest.R
import com.stanger.listquest.data.models.UserModel
import com.stanger.listquest.databinding.FragmentDeleteAccountBinding


class DeleteAccountFragment(var userModel: UserModel): Fragment() {
    private lateinit var viewModel: DeleteAccountFragmentViewModel
    lateinit var binding: FragmentDeleteAccountBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(arg0: Bundle?) {
        super.onActivityCreated(arg0)

        setUpViewModel()
    }

    private fun setUpViewModel(){
        viewModel = ViewModelProviders.of(this,
            DeleteAccountFragmentViewModelFactory(
                fragmentManager!!,
                userModel,
                context!!,
                activity!!.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE))
        ).get(DeleteAccountFragmentViewModel::class.java)

        binding.setVariable(BR.viewModel, viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_delete_account, container, false)
        binding.lifecycleOwner = this
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
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