package com.stanger.listquest.ui.profile


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.hardware.camera2.CameraCharacteristics
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.storage.FirebaseStorage
import com.stanger.listquest.BR
import com.stanger.listquest.R
import com.stanger.listquest.data.models.UserModel
import com.stanger.listquest.databinding.FragmentProfileBinding
import com.stanger.listquest.rotate
import com.stanger.listquest.utils.CameraListener
import com.stanger.listquest.utils.FirestoreRepository
import java.io.ByteArrayOutputStream
import java.io.IOException

class ProfileFragment(var userModel: UserModel,
                      val userPercent: Int,
                      val photoCallback: CameraListener
) : Fragment(),
    CameraListener {
    private val REQUEST_IMAGE_CAPTURE = 111
    private val READ_EXTERNAL_STORAGE = 101
    private val RC_SIGN_IN = 100

    var firebaseRepository = FirestoreRepository()
    private lateinit var viewModel: ProfileFragmentViewModel
    lateinit var binding: FragmentProfileBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun takePhoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { pictureIntent ->
            pictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);

            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1 && Build.VERSION.SDK_INT < Build.VERSION_CODES.O -> {
                    pictureIntent.putExtra("android.intent.extras.CAMERA_FACING", CameraCharacteristics.LENS_FACING_FRONT)  // Tested on API 24 Android version 7.0(Samsung S6)
                }
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                    pictureIntent.putExtra("android.intent.extras.CAMERA_FACING", CameraCharacteristics.LENS_FACING_FRONT) // Tested on API 27 Android version 8.0(Nexus 6P)
                    pictureIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true)
                }
                else -> pictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1)  // Tested API 21 Android version 5.0.1(Samsung S4)
            }

            pictureIntent.resolveActivity(activity?.packageManager!!)?.also {
                startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val bitmap : Bitmap = data?.extras?.get("data") as Bitmap
            uploadImageAndSaveUri(bitmap.rotate(-90F))
        }

        if (requestCode == READ_EXTERNAL_STORAGE && resultCode == RESULT_OK) {
            val selectedFile = data?.data
            try {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(activity!!.contentResolver, selectedFile);
                uploadImageAndSaveUri(bitmap)

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun uploadPhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent()
                .setType("image/*")
                .setAction(Intent.ACTION_GET_CONTENT).also { photoIntent ->
                    startActivityForResult(
                        photoIntent,
                        READ_EXTERNAL_STORAGE
                    )
                }
        }
    }

    private fun uploadImageAndSaveUri(bitmap: Bitmap){
        val baos = ByteArrayOutputStream()
        val storageRef = FirebaseStorage.getInstance().reference.child("photos").child(userModel.id)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val image = baos.toByteArray()
        val upload = storageRef.putBytes(image)

        upload.addOnCompleteListener{ uploadTask ->
            if(uploadTask.isSuccessful){
                storageRef.downloadUrl.addOnCompleteListener { urlTask ->
                    urlTask.result?.let {
                        viewModel.profilePicturePath.value = it.toString()
                        Toast.makeText(context, "Success!!", Toast.LENGTH_LONG).show()

                        userModel.profilePicturePath = userModel.id
                        userModel.isImgFromGoogleOrFacebook = false
                        firebaseRepository.updateUser(userModel)

                        firebaseRepository.storeImage(it)
                        photoCallback.passPhoto(it.toString())

                    }
                }
            }else{
                uploadTask.exception?.let {
                    Toast.makeText(context, userModel.profilePicturePath, Toast.LENGTH_LONG).show()
                }
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()

    }

//    private fun setUpAdjustView(){
//        about_me_txt.imeOptions = EditorInfo.IME_ACTION_DONE
//        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
//
//        KeyboardEventListener(activity as AppCompatActivity) { isOpen ->
//            if(about_me_txt != null) about_me_txt.isCursorVisible = isOpen
//            userModel.aboutMe = viewModel.userAboutMe.value!!
//            firebaseRepository.updateUser(userModel)
//        }
//    }

    private fun setUpViewModel(){
        viewModel = ViewModelProviders.of(this,
            ProfileFragmentViewModelFractory(userModel, fragmentManager!!, userPercent, this, context!!)).get(ProfileFragmentViewModel::class.java)
        binding.setVariable(BR.viewModel, viewModel)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding.lifecycleOwner = this

        return binding.root
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        var view = activity!!.currentFocus
//        if (view == null) view = View(activity)
//        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(view.windowToken, 0)
//    }
}