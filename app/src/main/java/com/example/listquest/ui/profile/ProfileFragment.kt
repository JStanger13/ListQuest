package com.example.listquest.ui.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.listquest.R
import com.example.listquest.data.utils.FirestoreUtil
import com.example.listquest.data.utils.StorageUtil
import com.example.listquest.ui.mainquest.MainQuestFragment
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.io.ByteArrayOutputStream

class ProfileFragment : Fragment() {
    private val RC_SELECT_IMAGE = 2
    private lateinit var selectedImageBytes: ByteArray
    private var pictureJustChanged = false


    companion object {
        fun newInstance() = ProfileFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view = inflater.inflate(R.layout.fragment_profile, container, false)

        view.apply{
            imageView_profile_picture.setOnClickListener{
                val intent = Intent().apply{
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
                }
                startActivityForResult(Intent.createChooser(intent, "Select Image"), RC_SELECT_IMAGE)
            }
            btn_save.setOnClickListener{
                if(::selectedImageBytes.isInitialized)
                    StorageUtil.uploadProfilePhoto(selectedImageBytes){ imagePath ->
                        FirestoreUtil.updateCurrentUser(edit_name.text.toString(),
                            imagePath)

                        switchFrag()
                    }

                else
                    StorageUtil.uploadProfilePhoto(selectedImageBytes){ imagePath ->
                        FirestoreUtil.updateCurrentUser(edit_name.text.toString(),
                            null)
            }

                btn_sign_out.setOnClickListener{
                    AuthUI.getInstance()
                        .signOut(this@ProfileFragment.context!!)
                       //TODO switch fragment to Login Fragment

                }
            }
        }

        return view
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK && data != null){
            val selectedImagePath = data.data
            val selectedImageBmp= MediaStore.Images.Media.getBitmap(activity?.contentResolver, selectedImagePath)

            val outputStream = ByteArrayOutputStream()
            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            selectedImageBytes = outputStream.toByteArray()


            Glide.with(this)
                .load(selectedImageBytes)
                .into(imageView_profile_picture)
            pictureJustChanged = true
        }
    }

    override fun onStart() {
        super.onStart()
        FirestoreUtil.getCurrentUser { user ->
            if(this@ProfileFragment.isVisible) {
                edit_name.setText(user.userName)
                    if(!pictureJustChanged && user.profilePicturePath != null)
                        Glide.with(this)
                            .load(StorageUtil.pathToReference(user.profilePicturePath))
                            .placeholder(R.drawable.ic_account_circle_black)
                            .into(imageView_profile_picture)
            }
        }
    }

    private fun switchFrag(){
        activity!!.supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left)
            .replace(R.id.container, MainQuestFragment.newInstance(), "")
            .commit()
    }
}

