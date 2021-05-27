package com.stanger.listquest.ui.profile.photo_dialog


import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.stanger.listquest.BR
import com.stanger.listquest.R
import com.stanger.listquest.data.models.UserModel
import com.stanger.listquest.databinding.DialogPhotoBinding
import com.stanger.listquest.utils.CameraListener
import com.stanger.listquest.utils.FirestoreRepository


class PhotoDialogFragment(val userModel: UserModel,
                          val fm: FragmentManager,
                          val callback: CameraListener
): BottomSheetDialogFragment(){

    private val CAMERA_REQUEST = 111
    private val UPLOAD_REQUEST = 101

    var firebaseRepository = FirestoreRepository()
    private lateinit var viewModel: PhotoDialogFragmentViewModel
    lateinit var binding: DialogPhotoBinding

//    override fun getTheme(): Int {
//        return R.style.AppAlertTheme
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(arg0: Bundle?) {
        super.onActivityCreated(arg0)

        setUpViewModel()

        val metrics = DisplayMetrics()
        dialog!!.window!!.attributes.windowAnimations = R.style.DialogSlideAnim
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        dialog!!.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            (metrics.heightPixels * 0.30).toInt()
        )
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            PhotoDialogFragmentViewModelFactory(
                callback,
                dialog!!,
                context!!,
                this
            )
        ).get(PhotoDialogFragmentViewModel::class.java)
        binding.setVariable(BR.viewModel, viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_photo, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onResume() {
        val params = dialog!!.window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams

        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()

        var view = activity!!.currentFocus
        if (view == null) view = View(activity)
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray): Unit {
        if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            when (requestCode) {
                CAMERA_REQUEST -> viewModel.takePhoto()
                UPLOAD_REQUEST -> viewModel.uploadPhoto()
            }
        }
    }
}