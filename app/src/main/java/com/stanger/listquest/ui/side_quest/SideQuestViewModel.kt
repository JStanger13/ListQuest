package com.stanger.listquest.ui.side_quest


import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.stanger.listquest.BR
import com.stanger.listquest.data.models.MainQuestModel
import com.stanger.listquest.data.models.SideQuestModel
import com.stanger.listquest.data.models.UserModel
import com.stanger.listquest.ui.calendar.CalenderFragment
import com.stanger.listquest.ui.side_quest.create_side_quest_dialog.CreateSideQuestDialogFragment
import com.stanger.listquest.utils.*
import kotlin.math.roundToInt

class SideQuestViewModel(var currentMainQuestModel: MainQuestModel,
                         var resources: Resources,
                         var fm: FragmentManager,
                         var context: Context,
                         val userModel: UserModel
): BaseObservable(),
    CreateSideQuestListener,
    SideEditQuestListener,
    CreateQuestListener {

    var firebaseRepository = FirestoreRepository()
    val bossImg = MutableLiveData<Drawable>()
    val bossHead = MutableLiveData<Drawable>()
    val bossName = MutableLiveData<String>()
    var numOfSideQuests =  MutableLiveData<String>()
    val numOfCompletedSideQuest = MutableLiveData<String>()
    var mainQuestTitle = MutableLiveData<String>()
    var questType = MutableLiveData<String>()
    var numOfNum = MutableLiveData<String>()
    var bossHealthPercent = MutableLiveData<String>()
    var bossHealthPercentFloat = MutableLiveData<Int>()
    var notesText = MutableLiveData<String>()
    var bossIsShown = MutableLiveData<Boolean>()
    var isAddButtonItem = MutableLiveData<Boolean>()
    var isListEmpty = MutableLiveData<Boolean>()
    val date = MutableLiveData<String>()
    val editIsFocusable = MutableLiveData<Boolean>()
    val editTitleIsFocusable = MutableLiveData<Boolean>()
    val editMaxLines = MutableLiveData<Int>()
    val notes = MutableLiveData<String>()
    private var _isFavorite= currentMainQuestModel.favorite
    val showDateCancel = MutableLiveData<Boolean>()

    init {
        editMaxLines.value = 1
        notes.value = currentMainQuestModel.notes
        editIsFocusable.value = false
        isListEmpty.value = currentMainQuestModel.questSize == 0
        mainQuestTitle.value = currentMainQuestModel.mainQuestTitle
        numOfSideQuests.value = currentMainQuestModel.questSize.toString() + QUESTS_AVAILABLE
        numOfCompletedSideQuest.value = currentMainQuestModel.bossHealth.toString() + QUESTS_COMPLETED
        //numOfNum.value = currentMainQuestModel.bossHealth.toString() + "/" + currentMainQuestModel.questSize.toString()
        getNumOfNum()
        bossName.value = currentMainQuestModel.boss
        bossImg.value = resources.getDrawable(BossUtils.changeBossImage(currentMainQuestModel.bossImg), null)
        bossHead.value = resources.getDrawable(BossUtils.getBossHead(currentMainQuestModel.bossImg), null)
        questType.value = "SideQuests"
        bossHealthPercent.value = currentMainQuestModel.bossPercent.toString() + "%"
        bossHealthPercentFloat.value = currentMainQuestModel.bossPercent
        notesText.value = currentMainQuestModel.notes
        bossName.value = currentMainQuestModel.boss
        val firebaseRepository = FirestoreRepository()

        bossHandler()
        getDate()
    }

    fun onItemClicked(sideQuest: SideQuestModel, title: String){
        sideQuest.sideQuestTitle = title

    }

    private fun getDate(){
        if(currentMainQuestModel.eventDate.isEmpty()){
            date.value = "no date selected"
            showDateCancel.value = false
        }else {
            date.value = currentMainQuestModel.eventDate + getTime()
            showDateCancel.value = true
        }
    }

    fun deleteDateAndTime(){
        currentMainQuestModel.eventTime = ""
        currentMainQuestModel.eventDate = ""
        firebaseRepository.editMainQuest(currentMainQuestModel, userModel)
        getDate()
    }

    private fun getTime(): String{
        return if(currentMainQuestModel.eventTime.isNotEmpty()){
            ", " + currentMainQuestModel.eventTime
        } else{
            ""
        }
    }

    fun editDate(){
        DialogUtils.launchCalendarDialog(fm, CalenderFragment(this,
            currentMainQuestModel.bossImg,
            currentMainQuestModel.eventDate,
            currentMainQuestModel.eventTime,
            true,
            currentMainQuestModel,
            currentMainQuestModel.mainQuestTitle)
        )
    }

    fun dismissDialog(){
        fm.popBackStack()
    }

    fun editOnClick(){
        editIsFocusable.value = true
    }

    fun editTitleOnClick(){
        editTitleIsFocusable.value = true
    }

//    var isFavorite: Boolean
//        @Bindable get() = _isFavorite
//        @Bindable set(value) {
//            _isFavorite = value
//            archiveCompletedQuest()
//            notifyPropertyChanged(BR.favorite)}

    fun archiveCompletedQuest(){
        currentMainQuestModel.favorite = !currentMainQuestModel.favorite
        firebaseRepository.editMainQuest(currentMainQuestModel, userModel)
        if(currentMainQuestModel.favorite) firebaseRepository.addFavoriteQuestToFirestore(currentMainQuestModel, userModel)
        if(!currentMainQuestModel.favorite) firebaseRepository.deleteFavoriteQuestToFirestore(currentMainQuestModel, userModel)
    }

    private fun getNumOfNum(){
        numOfNum.value = "hp: " + currentMainQuestModel.bossHealth.toString() + "/" + currentMainQuestModel.questSize.toString()
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
        getNumOfNum()
    }

    fun launchCreateSideQuest(){
        DialogUtils.launchCreateSideQuestDialog(fm, CreateSideQuestDialogFragment(this).newInstance(currentMainQuestModel.id))
    }


    fun getSideQuestQuery(mainQuestModel: MainQuestModel): FirestoreRecyclerOptions<SideQuestModel> {
        return FirestoreRecyclerOptions.Builder<SideQuestModel>()
            .setQuery(firebaseRepository.getSideQuestQuery(mainQuestModel.id, userModel), SideQuestModel::class.java).build()
    }

    private fun editMainQuest() = firebaseRepository.editMainQuest(currentMainQuestModel, userModel)
    private fun editFavoritesQuest() = firebaseRepository.editFavoritesQuest(currentMainQuestModel, userModel)


    private fun getNumOfAvailableSideQuests() {
        firebaseRepository.getSideQuestQuery(currentMainQuestModel.id, userModel)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                } else {
                    currentMainQuestModel.questSize = snapshots!!.size()
                    numOfSideQuests.value = currentMainQuestModel.questSize.toString() + QUESTS_AVAILABLE
                    isListEmpty.value = currentMainQuestModel.questSize == 0
                    getBossHealthPercent()
                    bossIsShown.value = currentMainQuestModel.questSize != 0
                    editMainQuest()
                    if(currentMainQuestModel.favorite)editFavoritesQuest()
                    getNumOfNum()
                }
            }
    }

    private fun getNumOfCompletedSideQuests() {
        firebaseRepository.getSideQuestChkQuery(currentMainQuestModel.id, userModel)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                } else {
                    currentMainQuestModel.bossHealth = snapshots!!.size()
                    numOfCompletedSideQuest.value = currentMainQuestModel.bossHealth.toString() + QUESTS_COMPLETED
                    getBossHealthPercent()
                    editMainQuest()
                    if(currentMainQuestModel.favorite)editFavoritesQuest()
                    getNumOfNum()
                }
            }
    }

    private fun getSideQuestID(): String{return firebaseRepository.getMainQuestId()}

    companion object {
        const val QUESTS_COMPLETED = " compleated"
        const val QUESTS_AVAILABLE = " total"
        const val TAG = "FIRESTORE_VIEW_MODEL"
    }

    override fun editSideQuestFromDialog(sideQuest: SideQuestModel) {
        firebaseRepository.editSideQuest(currentMainQuestModel, sideQuest, userModel)
    }

    override fun addMainQuestFromDialog(mainQuestModel: MainQuestModel) {

    }


    override fun editMainQuestFromDialog(mainQuestModel: MainQuestModel) {
        firebaseRepository.editMainQuest(currentMainQuestModel, userModel)
        getDate()
    }

    override fun addSideQuestFromDialog(id: String, newSideQuestTitle: String) {
        val side = SideQuestModel(
            getSideQuestID(),
            newSideQuestTitle,
            false,
            Timestamp.now())
        firebaseRepository.addSideQuestToFirestore(currentMainQuestModel, side, userModel)
        if(currentMainQuestModel.favorite)  firebaseRepository.addSideQuestToFavorites(currentMainQuestModel, side, userModel)
        bossHandler()
        bossIsShown.value = true
    }
}