package com.example.listquest.ui.dialogs

import android.content.Context
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
import com.example.listquest.BR
import com.example.listquest.R
import com.example.listquest.data.models.UserModel
import com.example.listquest.data.utils.FirestoreRepository
import com.example.listquest.databinding.DialogPhotoBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PhotoDialogFragment(val userModel: UserModel,
                          val fm: FragmentManager,
                          val callback: PhotoDialogFragmentViewModel.CameraListener
): BottomSheetDialogFragment(){
    var firebaseRepository = FirestoreRepository()
    private lateinit var viewModel: PhotoDialogFragmentViewModel
    lateinit var binding: DialogPhotoBinding


    override fun getTheme(): Int {
        return R.style.AppAlertTheme
    }

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
            PhotoDialogFragmentViewModelFactory(userModel, fragmentManager!!, callback, dialog!!)
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

//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun takePhoto() {
//        if (checkPersmission()) {
//            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { pictureIntent ->
//
//                when {
//                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1 && Build.VERSION.SDK_INT < Build.VERSION_CODES.O -> {
//                        pictureIntent.putExtra(
//                            "android.intent.extras.CAMERA_FACING",
//                            CameraCharacteristics.LENS_FACING_FRONT
//                        )  // Tested on API 24 Android version 7.0(Samsung S6)
//                    }
//                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
//                        pictureIntent.putExtra(
//                            "android.intent.extras.CAMERA_FACING",
//                            CameraCharacteristics.LENS_FACING_FRONT
//                        ) // Tested on API 27 Android version 8.0(Nexus 6P)
//                        pictureIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true)
//                    }
//                    else -> pictureIntent.putExtra(
//                        "android.intent.extras.CAMERA_FACING",
//                        1
//                    )  // Tested API 21 Android version 5.0.1(Samsung S4)
//                }
//
//                pictureIntent.resolveActivity(activity?.packageManager!!)?.also {
//                    startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE)
//                }
//            }
//        } else requestPermission()
//    }
//
//    private fun checkPersmission(): Boolean {
//        return (ContextCompat.checkSelfPermission(context!!, android.Manifest.permission.CAMERA) ==
//                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
//            context!!,
//            Manifest.permission.READ_EXTERNAL_STORAGE
//        ) == PackageManager.PERMISSION_GRANTED)
//    }
//
//    private fun requestPermission() {
//        ActivityCompat.requestPermissions(
//            activity!!, arrayOf(
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.CAMERA
//            ),
//            PERMISSION_REQUEST_CODE
//        )
//    }
//
//
//    override fun uploadPhoto() {
//        if (!checkPersmission()) {
//            requestPermission()
//        } else {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                //val intent =
//                    Intent()
//                        .setType("image/*")
//                        .setAction(Intent.ACTION_GET_CONTENT).also { photoIntent ->
//                            //Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { pictureIntent ->
//                            startActivityForResult(
//                                photoIntent,
//                                PERMISSION_REQUEST_CODE
//                            )
//                        }
//            }
//        }
//    }
//
//
//    private fun uploadImageAndSaveUri(bitmap: Bitmap){
//        val baos = ByteArrayOutputStream()
//        //fix this
//        val photoId = firebaseRepository.getMainQuestId()
//        val storageRef = FirebaseStorage.getInstance().reference.child("photos").child(photoId)
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//        val image = baos.toByteArray()
//        val upload = storageRef.putBytes(image)
//
//
//        upload.addOnCompleteListener{ uploadTask ->
//            if(uploadTask.isSuccessful){
//                storageRef.downloadUrl.addOnCompleteListener { urlTask ->
//                    urlTask.result?.let {
//
//                        Toast.makeText(context, "Success!!", Toast.LENGTH_LONG).show()
//
//                        photoModel.path = it.toString()
//                        photoModel.id = photoId
//                        userModel.profilePicturePath = photoId
//                        userModel.isImgFromGoogleOrFacebook = false
//                        firebaseRepository.updateUser(userModel)
//
//                        DatabaseUtils.addPictureToFirestore(photoId, it.toString(), false)
//                        firebaseRepository.storeImage(it)
//                    }
//                }
//            }else{
//                uploadTask.exception?.let {
//                    Toast.makeText(context, userModel.profilePicturePath, Toast.LENGTH_LONG).show()
//                }
//            }
//
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 111 && resultCode == Activity.RESULT_OK) {
//            val x =1
//        }
//       if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
//            val selectedFile = data?.data //The uri with the location of the file
//
//            try {
//                val bitmap =
//                    MediaStore.Images.Media.getBitmap(activity!!.contentResolver, selectedFile);
//
//                    uploadImageAndSaveUri(bitmap)
//
//            } catch (e: IOException) {
//                e.printStackTrace()
//                Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
}