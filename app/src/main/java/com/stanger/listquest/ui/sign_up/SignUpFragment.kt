package com.stanger.listquest.ui.sign_up


import android.content.Context
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
import com.stanger.listquest.databinding.FragmentSignUpBinding
import com.stanger.listquest.utils.FirestoreRepository


class SignUpFragment: Fragment() {
    private lateinit var viewModel: SignUpViewModel
    lateinit var binding: FragmentSignUpBinding
    var firebaseRepository = FirestoreRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_sign_up,
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
            SignUpViewModelFactory(
                fragmentManager!!, context!!, activity!!.getSharedPreferences("loginPrefs",
                    Context.MODE_PRIVATE))
        ).get(SignUpViewModel::class.java)
        binding.setVariable(BR.viewModel, viewModel)
    }
}