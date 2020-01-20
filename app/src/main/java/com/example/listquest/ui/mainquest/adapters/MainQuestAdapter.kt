package com.example.listquest.ui.mainquest

import android.content.res.Resources
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.listquest.BR
import com.example.listquest.R
import com.example.listquest.data.models.MainQuestModel
import com.example.listquest.data.utils.FirestoreRepository
import com.example.listquest.databinding.ItemMainQuestBinding
import com.example.listquest.ui.mainquest.item_viewmodel.MainQuestItemViewModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot

class MainQuestAdapter internal constructor(options: FirestoreRecyclerOptions<MainQuestModel>,
                                            val viewModel: MainQuestViewModel,
                                            val resources: Resources) :
    FirestoreRecyclerAdapter<MainQuestModel, MainQuestAdapter.MyViewHolder>(options) {

    var firebaseRepository = FirestoreRepository()

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, mainQuest: MainQuestModel) {
        holder.bind(mainQuest)
        holder.binding.chkBx.setOnClickListener{
            archiveCompletedQuest(deleteSideQuest(position, true))
        }
    }

    private fun archiveCompletedQuest(mainQuestModel: MainQuestModel) {
        mainQuestModel.completed = true
        firebaseRepository.addCompletedMainQuest(mainQuestModel)
        if(mainQuestModel.favorite) firebaseRepository.deleteFavoriteQuestToFirestore(mainQuestModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = DataBindingUtil.inflate<ItemMainQuestBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_main_quest, parent, false)
        return MyViewHolder(binding)
    }

    fun deleteSideQuest(position: Int, boolean: Boolean): MainQuestModel {
        val snap: DocumentSnapshot = snapshots.getSnapshot(position)
        val mainQuestModel: MainQuestModel = snap.toObject(MainQuestModel::class.java)!!
        snap.reference.delete()

        if(mainQuestModel.favorite) firebaseRepository.deleteFavoriteQuestToFirestore(mainQuestModel)
        if(boolean) viewModel.updateUserAdd()
//        else viewModel.updateUserDelete()

        return mainQuestModel
    }


    inner class MyViewHolder internal constructor(val binding: ItemMainQuestBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(mainQuest: MainQuestModel) {

            binding.setVariable(BR.mainQuest, mainQuest)
            binding.setVariable(BR.viewModel, viewModel)
            binding.setVariable(BR.mainViewModel,
                MainQuestItemViewModel(
                    mainQuest,
                    resources)
            )
            if(mainQuest.completed) binding.title.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            else binding.title.paintFlags = 0
        }
    }
}