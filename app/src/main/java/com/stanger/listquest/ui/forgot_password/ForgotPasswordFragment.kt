package com.stanger.listquest.ui.forgot_password


import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.stanger.listquest.BR
import com.stanger.listquest.R
import com.stanger.listquest.databinding.FragmentForgotPasswordBinding


class ForgotPasswordFragment: Fragment() {
    private lateinit var viewModel: ForgotPasswordFragmentViewModel
    lateinit var binding: FragmentForgotPasswordBinding
//    var firebaseRepository = FirestoreRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_forgot_password,
            container,
            false
        )

        binding.lifecycleOwner = this
        binding.apply {}

        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpViewModel()
    }


    private fun setUpViewModel() {
        viewModel = ViewModelProviders.of(this,
            ForgotPasswordFragmentViewModelFactory(context!!, fragmentManager!!)
        ).get(ForgotPasswordFragmentViewModel::class.java)

        binding.setVariable(BR.viewModel, viewModel)
    }
}