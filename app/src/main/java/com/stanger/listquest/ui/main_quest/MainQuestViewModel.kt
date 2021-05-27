package com.stanger.listquest.ui.main_quest


import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.stanger.listquest.R
import com.stanger.listquest.data.models.MainQuestModel
import com.stanger.listquest.data.models.UserModel
import com.stanger.listquest.ui.main_quest.create_main_quest_dialog.CreateMainQuestDialogFragment
import com.stanger.listquest.ui.profile.ProfileFragment
import com.stanger.listquest.ui.side_quest.SideQuestFragment
import com.stanger.listquest.ui.side_quest.SideQuestViewModel
import com.stanger.listquest.utils.*
import com.stanger.listquest.utils.FirestoreUtil.isLevelUp
import kotlin.math.roundToInt

class MainQuestViewModel(
    var res: Resources,
    var fm: FragmentManager,
    val cl: CoordinatorLayout,
    val frag: MainQuestFragment,
    val userModel: UserModel
) : ViewModel(),
    CreateQuestListener,
    EditQuestListener,
    FavoriteListener,
    CameraListener {

    private val firestoreInstance:  FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val photoRef: DocumentReference
        get() = firestoreInstance.collection("images").document(userModel.profilePicturePath)

    private val TAG = "FIRESTORE_VIEW_MODEL"
    var isListEmpty = MutableLiveData<Boolean>()
    var isCompletedListEmpty = MutableLiveData<Boolean>()
    var isFavoriteListEmpty = MutableLiveData<Boolean>()
    var userName = MutableLiveData<String>()
    var userLvl = MutableLiveData<String>()
    var userPercent = MutableLiveData<String>()
    var userPercentBar = MutableLiveData<Int>()
    var userCompleted = MutableLiveData<Int>()
    var firebaseRepository = FirestoreRepository()
    var profilePicturePath = MutableLiveData<String>()
    var errorImg = MutableLiveData<Int>()

    init {
        getUserInfo()
        getNumOfQuests()
        getNumOfCompletedQuests()
        getNumOfFavoriteQuests()
        errorImg.value = R.drawable.profile_no_img
        profilePicturePath.value = userModel.profilePicturePath
        getImgFromFirestore()
    }
    //Photo Methods
    override fun passPhoto(path: String) {
        super.passPhoto(path)
        profilePicturePath.value = path
        this.userModel.profilePicturePath = path
    }

    private fun getImgFromFirestore() {
        profilePicturePath.value = ""

        if (userModel.isImgFromGoogleOrFacebook) profilePicturePath.value =
            userModel.profilePicturePath
        else
            FirebaseStorage.getInstance().reference.child("photos").child(userModel.id).downloadUrl.addOnSuccessListener {
                profilePicturePath.value = it.toString()
            }.addOnFailureListener {

            }
    }

    //Add//Delete Item
    fun updateUserAdd() {
        userModel.levelUpNumerator = userModel.levelUpNumerator + 1
        if (userModel.levelUpNumerator > 0) {
            if ((userModel.levelUpNumerator / userModel.levelUpDenominator) == 1) {
                userModel.levelUpNumerator = 0
                userModel.levelUpDenominator++
                userModel.lvl++
                isLevelUp = true
            }
        }
        userModel.numQuestsAvail--
        userModel.numQuestsCompleted++
        firebaseRepository.updateUser(userModel)
        getUserInfo()
    }

    fun updateUserDelete() {
        userModel.levelUpNumerator--
        if (userModel.levelUpNumerator < 0) {
            userModel.levelUpDenominator--
            userModel.levelUpNumerator = userModel.levelUpDenominator - 1
            userModel.lvl--
        }
        userModel.numQuestsAvail++
        userModel.numQuestsCompleted--
        firebaseRepository.updateUser(userModel)
        getUserInfo()
    }

    fun getUserInfo() {
        userName.value = userModel.userName
        userLvl.value = userModel.lvl.toString()
        userPercent.value = userModel.getPercent().toString()
        userCompleted.value = userModel.numQuestsCompleted
        getNumOfQuests()
        getProgressBarData()

        if (isLevelUp) {
            Snackbar.make(cl, "you've leveled up!", Snackbar.LENGTH_LONG).show()

            isLevelUp = false
        }
    }

    //set up
    fun getQuestDate(date: String, time: String): String {
        return date + checkIfTimeIsSet(time)
    }

    fun showCalendarIc(date: String): Boolean {
        return date.isNotEmpty()
    }

    private fun checkIfTimeIsSet(time: String): String {
        if (time.isEmpty()) {
            return time
        }
        return ", $time"
    }

    private fun getProgressBarData() {
        if (userModel.levelUpNumerator <= 0) {
            userPercent.value = "0%"
            userPercentBar.value = 0
        }
        val progressValue =
            ((userModel.levelUpNumerator.toDouble() / userModel.levelUpDenominator.toDouble()) * 100).roundToInt()
        userPercent.value = "$progressValue%"
        userPercentBar.value = progressValue
    }

    override fun addMainQuestFromDialog(mainQuestModel: MainQuestModel) {
        addMainQuest(mainQuestModel)
        userModel.numQuestsAvail++
        firebaseRepository.updateUser(userModel)

        getUserInfo()
    }

    override fun editMainQuestFromDialog(mainQuestModel: MainQuestModel) {
        firebaseRepository.editMainQuest(mainQuestModel, userModel)
        if (mainQuestModel.favorite) firebaseRepository.editFavoritesQuest(
            mainQuestModel,
            userModel
        )
    }

    fun createMainQuest() = DialogUtils.launchCreateMainQuestDialog(
        fm,
        CreateMainQuestDialogFragment(this, BossUtils.generateBoss(), "", "", "").newInstance()
    )

    fun convertToDrawable(bossImg: Int): Drawable {
        return res.getDrawable(BossUtils.getBossHead(bossImg), null)
    }

    private fun addMainQuest(mainQuest: MainQuestModel) {
        firebaseRepository.addMainQuestToFirestore(mainQuest, userModel).addOnFailureListener {
            Log.e(TAG, "Failed to save Address!")
        }
        getUserInfo()
    }

    private fun getNumOfQuests() {
        firebaseRepository.getMainQuestQuery(userModel)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(SideQuestViewModel.TAG, "Listen failed.", e)
                } else {
                    isListEmpty.value = snapshots!!.isEmpty
                    userModel.numQuestsAvail = snapshots.size()
                }
                getProgressBarData()
                userLvl.value = userModel.lvl.toString()
            }
    }

    private fun getNumOfCompletedQuests() {
        firebaseRepository.getCompletedQuestsQuery(userModel)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(SideQuestViewModel.TAG, "Listen failed.", e)
                } else {
                    isCompletedListEmpty.value = snapshots!!.isEmpty
                }
                getProgressBarData()
            }
    }

    private fun getNumOfFavoriteQuests() {
        firebaseRepository.getFavoritesQuestsQuery(userModel)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(SideQuestViewModel.TAG, "Listen failed.", e)
                } else {
                    isFavoriteListEmpty.value = snapshots!!.isEmpty
                }
            }
    }

    override fun editQuestFromDialog(mainQuestModel: MainQuestModel) =
        firebaseRepository.editMainQuest(mainQuestModel, userModel)

    private fun getMainQuestId(): String {
        return firebaseRepository.getMainQuestId()
    }

    fun onItemClick(mainQuest: MainQuestModel) {
        if (!mainQuest.completed) DialogUtils.launchSideQuestFragment(
            fm,
            SideQuestFragment(userModel),
            mainQuest
        )
    }

    fun showProfileDialog() = DialogUtils.launchProfileFragment(
        fm,
        ProfileFragment(userModel, userPercentBar.value!!, this)
    )

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