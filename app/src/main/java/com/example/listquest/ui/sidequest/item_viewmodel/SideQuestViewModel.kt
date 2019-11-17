package com.example.listquest.ui.sidequest.item_viewmodel

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.listquest.data.models.MainQuestModel
import com.example.listquest.data.models.SideQuestModel
import com.example.listquest.data.utils.BossUtils
import com.example.listquest.data.utils.DatabaseUtils
import com.example.listquest.data.utils.DialogUtils
import com.example.listquest.data.utils.FirestoreRepository
import com.example.listquest.data.utils.FirestoreUtil.isLevelUp
import com.example.listquest.data.utils.FirestoreUtil.userModel
import com.example.listquest.ui.sidequest.create_side_quest.CreateSideDialogFragment
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import kotlin.math.roundToInt

class SideQuestViewModel(var currentMainQuestModel: MainQuestModel,
                         var resources: Resources,
                         var fm: FragmentManager,
                         var context: Context): ViewModel(),
    CreateSideDialogFragment.CreateSideQuestListener{

    val QUESTS_COMPLETED = " Quests Compleated"
    val QUESTS_AVAILABLE = " Quests Available"
    val TAG = "FIRESTORE_VIEW_MODEL"
    var firebaseRepository = FirestoreRepository()
    val bossImg = MutableLiveData<Drawable>()
    val bossName = MutableLiveData<String>()
    var numOfSideQuests =  MutableLiveData<String>()
    val numOfCompletedSideQuest = MutableLiveData<String>()
    var mainQuestTitle = MutableLiveData<String>()
    var questType = MutableLiveData<String>()
    var bossHealthPercent = MutableLiveData<String>()
    var bossHealthPercentFloat = MutableLiveData<Int>()
    var notesText = MutableLiveData<String>()
    var bossIsShown = MutableLiveData<Boolean>()

    init {
        mainQuestTitle.value = currentMainQuestModel.mainQuestTitle
        numOfSideQuests.value = currentMainQuestModel.questSize.toString() + QUESTS_AVAILABLE
        numOfCompletedSideQuest.value = currentMainQuestModel.bossHealth.toString() + QUESTS_COMPLETED
        bossName.value = currentMainQuestModel.boss
        bossImg.value = resources.getDrawable(BossUtils.changeBossImage(currentMainQuestModel.bossImg), null)
        questType.value = "SideQuests"
        bossHealthPercent.value = currentMainQuestModel.bossPercent.toString() + "%"
        bossHealthPercentFloat.value = currentMainQuestModel.bossPercent
        notesText.value = currentMainQuestModel.notes
        bossName.value = currentMainQuestModel.boss
        bossHandler()
    }

    override fun addSideQuestFromDialog(id: String, newSideQuestTitle: String) {
        val side = SideQuestModel(
            getSideQuestID(),
            newSideQuestTitle,
            false,
            Timestamp.now())
        DatabaseUtils.addSideQuestToFirestore(id, side)
        bossHandler()
        bossIsShown.value = true
    }

    private fun getBossHealthPercent(){
        if(currentMainQuestModel.questSize > 0 && currentMainQuestModel.bossHealth > 0) {
            currentMainQuestModel.bossPercent = ((currentMainQuestModel.bossHealth.toDouble() / currentMainQuestModel.questSize.toDouble())* 100).roundToInt()
        } else {
            currentMainQuestModel.bossPercent = 0
        }
        bossHealthPercent.value = currentMainQuestModel.bossPercent.toString() + "%"
        bossHealthPercentFloat.value = currentMainQuestModel.bossPercent
    }

    private fun bossHandler(){
        getNumOfAvailableSideQuests()
        getNumOfCompletedSideQuests()
        bossIsShown.value = currentMainQuestModel.questSize > 0
    }

    fun launchCreateSideQuest(){
        DialogUtils.launchCreateSideQuestDialog(fm, CreateSideDialogFragment(this).newInstance(currentMainQuestModel.id))
    }


    fun getSideQuestQuery(mainQuestModel: MainQuestModel): FirestoreRecyclerOptions<SideQuestModel> {
        return FirestoreRecyclerOptions.Builder<SideQuestModel>()
            .setQuery(firebaseRepository.getSideQuestQuery(mainQuestModel.id), SideQuestModel::class.java).build()
    }

    private fun editMainQuest() =
        firebaseRepository.editMainQuest(currentMainQuestModel)


    private fun getNumOfAvailableSideQuests() {
        firebaseRepository.getSideQuestQuery(currentMainQuestModel.id)
            .addSnapshotListener(EventListener<QuerySnapshot> { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                } else {
                    currentMainQuestModel.questSize = snapshots!!.size()
                    numOfSideQuests.value = currentMainQuestModel.questSize.toString() + QUESTS_AVAILABLE
                    getBossHealthPercent()
                    bossIsShown.value = currentMainQuestModel.questSize != 0
                    editMainQuest()
                }
            })
    }

    private fun getNumOfCompletedSideQuests() {
        firebaseRepository.getSideQuestChkQuery(currentMainQuestModel.id)
            .addSnapshotListener(EventListener<QuerySnapshot> { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                } else {
                    currentMainQuestModel.bossHealth = snapshots!!.size()
                    numOfCompletedSideQuest.value = currentMainQuestModel.bossHealth.toString() + QUESTS_COMPLETED
                    getBossHealthPercent()
                    editMainQuest()
                }
            })
    }

    private fun getSideQuestID(): String{return firebaseRepository.getMainQuestId()}

    fun archiveCompletedQuest(){
        DialogUtils.launchMainQuestFragment(fm)
        firebaseRepository.completeMainQuest(currentMainQuestModel)
        firebaseRepository.addCompletedMainQuest(currentMainQuestModel)
        firebaseRepository.deleteMainQuestToFirestore(currentMainQuestModel)
        userModel.levelUpNumerator++
        if((userModel.levelUpNumerator/userModel.levelUpDenominator) == 1){
            userModel.levelUpNumerator = 0
            userModel.levelUpDenominator++
            userModel.lvl++
            isLevelUp = true
        }
        userModel.numQuestsAvail--
        userModel.numQuestsCompleted++
        firebaseRepository.updateUser(userModel)
    }
}