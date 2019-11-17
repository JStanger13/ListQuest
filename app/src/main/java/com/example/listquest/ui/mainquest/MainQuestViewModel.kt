package com.example.listquest.ui.mainquest
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.listquest.R
import com.example.listquest.data.models.MainQuestModel
import com.example.listquest.data.utils.BossUtils
import com.example.listquest.data.utils.DialogUtils
import com.example.listquest.data.utils.FirestoreRepository
import com.example.listquest.data.utils.FirestoreUtil.isLevelUp
import com.example.listquest.data.utils.FirestoreUtil.userModel
import com.example.listquest.ui.dialogs.EditDialogFragment
import com.example.listquest.ui.dialogs.level_up.LevelUpDialogFragment
import com.example.listquest.ui.login.LoginFragment
import com.example.listquest.ui.mainquest.create_main_quest.CreateMainDialogFragment
import com.example.listquest.ui.mainquest.main_quest_more.MainQuestMoreDialog
import com.example.listquest.ui.sidequest.SideQuestFragment
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.roundToInt

class MainQuestViewModel(var res: Resources,
                         var fm: FragmentManager): ViewModel(),
    CreateMainDialogFragment.CreateQuestListener,
    EditDialogFragment.EditQuestListener{

    private val TAG = "FIRESTORE_VIEW_MODEL"
    var isListEmpty = MutableLiveData<Boolean>()
    var numOfMainQuests = MutableLiveData<String>()
    var userName = MutableLiveData<String>()
    var userLvl = MutableLiveData<String>()
    var userPercent = MutableLiveData<String>()
    var userPercentBar = MutableLiveData<Int>()
    var userCompleted = MutableLiveData<Int>()
    var firebaseRepository = FirestoreRepository()

    init {
        getUserInfo()
    }

    fun getUserInfo(){
        userName.value = userModel.userName
        userLvl.value = "Lvl: " + userModel.lvl.toString()
        userPercent.value = userModel.getPercent().toString()
        userCompleted.value = userModel.numQuestsAvail
        isListEmpty.value = userModel.numQuestsAvail <= 0
        getBossHealth()

        if(isLevelUp){
            DialogUtils.launchLvlUp(fm, LevelUpDialogFragment())
            isLevelUp = false
        }
    }

    fun onMainMorePressed(){
        DialogUtils.launchMainMoreDialog(fm, MainQuestMoreDialog())
    }

    fun getQuestDate(date: String, time: String): String{
        return date +  checkIfTimeIsSet(time)
    }


    fun showCalendarIc(date: String): Boolean {
        return date.isNotEmpty()
    }


    private fun checkIfTimeIsSet(time: String): String {
        if(time.isEmpty()){
            return time
        }
        return ", $time"
    }

    private fun getBossHealth(){
        val progressValue = ((userModel.levelUpNumerator.toDouble() / userModel.levelUpDenominator.toDouble())* 100).roundToInt()
        userPercent.value = "$progressValue%"
        userPercentBar.value = progressValue
    }

    override fun addMainQuestFromDialog(newMainQuestTitle: String, bossImg: Int, bossName: String, dateTxt: String, timeTxt: String) {
        val main = MainQuestModel(
            getMainQuestId(), newMainQuestTitle, bossName, bossImg,
            "",
            0,
            0,
            0,
            Timestamp.now(),
            "",
            false,
            dateTxt,
            timeTxt)
        addMainQuest(main)
        getNumOfAvailableMainQuests()
        userModel.numQuestsAvail++
        firebaseRepository.updateUser(userModel)
        isListEmpty.value = false
    }


    fun editMainQuest(mainQuestModel: MainQuestModel) =
        DialogUtils.launchEditMainDialog(fm, EditDialogFragment(this).newInstance(mainQuestModel))

    fun logOut(){
        FirebaseAuth.getInstance().signOut()
        fm.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left)
            .add(R.id.container, LoginFragment.newInstance(), "")
            .commitNow()
    }

    fun getNumOfAvailableMainQuests() {
        firebaseRepository.getMainQuestQuery()
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    numOfMainQuests.value = "0"
                    Log.w(TAG, "Listen failed.", e)
                } else {
                    numOfMainQuests.value =  snapshots!!.size().toString()
                }
            }
    }

    fun createMainQuest() =
        DialogUtils.launchCreateMainQuestDialog(fm, CreateMainDialogFragment(this,  BossUtils.generateBoss(), "", "").newInstance())

    fun convertToDrawable(bossImg: Int): Drawable {
        return res.getDrawable(BossUtils.getBossHead(bossImg), null)
    }

    private fun addMainQuest(mainQuest: MainQuestModel) {
        firebaseRepository.addMainQuestToFirestore(mainQuest).addOnFailureListener {
            Log.e(TAG, "Failed to save Address!")
        }
        getUserInfo()
    }

    override fun editQuestFromDialog(mainQuestModel: MainQuestModel) =
        firebaseRepository.editMainQuest(mainQuestModel)

    private fun getMainQuestId(): String {
        return firebaseRepository.getMainQuestId()
    }

    fun getMainQuestQuery(): FirestoreRecyclerOptions<MainQuestModel> {
        return FirestoreRecyclerOptions.Builder<MainQuestModel>()
            .setQuery(firebaseRepository.getMainQuestQuery(), MainQuestModel::class.java).build()
    }

    fun onItemClick(mainQuest: MainQuestModel) =
        DialogUtils.launchSideQuestFragment(fm, SideQuestFragment(), mainQuest)
}



