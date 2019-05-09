package com.example.listquest.ui.sidequest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.listquest.models.MainQuestModel
import com.example.listquest.models.SideQuestModel


class SideQuestViewModel: ViewModel(){

    fun addQuestItem(mainQuest: MainQuestModel,
                     string: String,
                     adapter: SideQuestFragment.SideQuestRecyclerView) {
        mainQuest.sideQuestList.add(SideQuestModel(string, false))
        adapter.notifyDataSetChanged()
    }

    private lateinit var isLoadingLiveData: MutableLiveData<Boolean>

    fun isLoading(): LiveData<Boolean> {
        if (!::isLoadingLiveData.isInitialized) {
            isLoadingLiveData = MutableLiveData()
            isLoadingLiveData.value = true
        }
        return isLoadingLiveData
    }

    private lateinit var isErrorLiveData: MutableLiveData<Boolean>

    fun isError(): LiveData<Boolean> {
        if (!::isErrorLiveData.isInitialized) {
            isErrorLiveData = MutableLiveData()
            isErrorLiveData.value = false
        }
        return isErrorLiveData
    }

    private lateinit var userLiveData: MutableLiveData<MainQuestModel>
}