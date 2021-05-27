package com.stanger.listquest.ui.login

import android.content.Context.MODE_PRIVATE
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
import com.stanger.listquest.databinding.FragmentLoginBinding
import com.stanger.listquest.utils.ErrorListener
import com.stanger.listquest.utils.FirestoreRepository


class LoginFragment: Fragment(), ErrorListener {

    private lateinit var viewModel: LoginFragmentViewModel
    lateinit var binding: FragmentLoginBinding
    var firebaseRepository = FirestoreRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login,
            container,
            false
        )

        binding.lifecycleOwner = this
//        binding.apply {}
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpViewModel()
    }

    companion object {
        fun newInstance() = LoginFragment()
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProviders.of(this,
            LoginFragmentViewModelFactory
                (fragmentManager!!,
                context!!,
                this,
                activity!!.getSharedPreferences("loginPrefs", MODE_PRIVATE))
        ).get(LoginFragmentViewModel::class.java)
        binding.setVariable(BR.viewModel, viewModel)
    }
}