package com.stanger.listquest.ui.main_quest.view_pager


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.stanger.listquest.MainActivity.Companion.firebaseRepository
import com.stanger.listquest.data.models.MainQuestModel
import com.stanger.listquest.data.models.UserModel


class PageViewModel(val userModel: UserModel) : ViewModel() {

    val _index = MutableLiveData<Int>()

    fun setIndex(index: Int) {
        _index.value = index
    }

    fun getMainQuestQuery(): FirestoreRecyclerOptions<MainQuestModel> {
        return FirestoreRecyclerOptions.Builder<MainQuestModel>()
            .setQuery(firebaseRepository.getMainQuestQuery(userModel), MainQuestModel::class.java).build()
    }

    fun getCompletedQuery(): FirestoreRecyclerOptions<MainQuestModel> {
        return FirestoreRecyclerOptions.Builder<MainQuestModel>()
            .setQuery(firebaseRepository.getCompletedQuestsQuery(userModel), MainQuestModel::class.java).build()
    }

    fun getFavoritesQuestsQuery(): FirestoreRecyclerOptions<MainQuestModel> {
        return FirestoreRecyclerOptions.Builder<MainQuestModel>()
            .setQuery(firebaseRepository.getFavoritesQuestsQuery(userModel), MainQuestModel::class.java).build()
    }
}