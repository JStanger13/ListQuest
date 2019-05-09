package com.example.listquest.ui.mainquest

import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.listquest.models.MainQuestModel
import com.example.listquest.models.SideQuestModel
import com.example.listquest.utils.KeyboardUtils
import kotlinx.android.synthetic.main.fragment_main_quest.*
import kotlinx.android.synthetic.main.fragment_main_quest.view.*

class MainQuestViewModel : ViewModel() {
    companion object {
        var mainQuestList = mutableListOf<MainQuestModel>()
    }

    fun addQuestItem(string: String, adapter: MainQuestFragment.MainQuestRecyclerView) {
        mainQuestList.add(MainQuestModel(string, "", "",0, mutableListOf<SideQuestModel>()))
        adapter.notifyItemInserted(mainQuestList.size - 1)
        adapter.notifyItemRangeChanged(mainQuestList.size - 1, mainQuestList.size)
    }
    fun getQuestList(): MutableList<MainQuestModel>{
        return mainQuestList
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
