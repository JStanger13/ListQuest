package com.stanger.listquest.ui.main_quest


import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import com.google.firebase.storage.FirebaseStorage
import com.stanger.listquest.BR
import com.stanger.listquest.R
import com.stanger.listquest.data.models.UserModel
import com.stanger.listquest.databinding.FragmentMainQuestBinding
import com.stanger.listquest.ui.main_quest.view_pager.LockableViewPager
import com.stanger.listquest.ui.main_quest.view_pager.SectionsPagerAdapter
import com.stanger.listquest.utils.FirestoreRepository
import kotlinx.android.synthetic.main.fragment_main_quest.*


class MainQuestFragment(val userModel: UserModel): Fragment(), View.OnClickListener{
    private lateinit var viewModel: MainQuestViewModel
    lateinit var binding: FragmentMainQuestBinding
    var firebaseRepository = FirestoreRepository()
    var profilePicturePath = MutableLiveData<String>()
    var errorImg = MutableLiveData<Int>()

    init {
        errorImg.value = R.drawable.profile_no_img
    }

    private fun getImgFromFirestore(path: String) {
        FirebaseStorage.getInstance().reference.child("photos").child(path).downloadUrl.addOnSuccessListener {
            profilePicturePath.value = it.toString()
        }.addOnFailureListener {
            //TODO fix here
        }

}
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main_quest,
            container,
            false
        )

        binding.lifecycleOwner = this
        binding.apply {}

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        viewModel.getUserInfo()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profilePicturePath.value = userModel.profilePicturePath
        getImgFromFirestore(userModel.profilePicturePath)
        setUpViewModel()
        setupViewPager()
//        arguments?.let {
//            userModel = it.getSerializable(ARG_USER) as UserModel
//            profilePicturePath.value = userModel.profilePicturePath
//            getImgFromFirestore(userModel.profilePicturePath)
//            setUpViewModel()
//            setupViewPager()
//        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun setupViewPager(){
        val sectionsPagerAdapter = SectionsPagerAdapter(context!!, childFragmentManager, userModel)
        val viewPager: LockableViewPager = view_pager as LockableViewPager
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = tabs
        tabs.setTabTextColors(
            resources.getColor(R.color.dark_blue),
            resources.getColor(R.color.dark_blue))
        tabs.setupWithViewPager(viewPager)
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            MainQuestViewModelFactory(resources,
                activity!!.supportFragmentManager,
                cl,
                this,
                userModel)
        ).get(MainQuestViewModel::class.java)

        binding.setVariable(BR.clickListener, this)
        binding.setVariable(BR.viewModel, viewModel)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.user_img_click -> viewModel.showProfileDialog()
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter(value = ["profileImage", "error"], requireAll = false)
        fun loadImage(view: ImageView, profileImage: String, error: Int) {
            Glide.with(view.context)
                .load(profileImage)
                .apply(RequestOptions.circleCropTransform())
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        view.setImageResource(error)
                        return true
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        view.setImageDrawable(resource)
                        return true
                    }
                }).into(view)
        }
    }
}