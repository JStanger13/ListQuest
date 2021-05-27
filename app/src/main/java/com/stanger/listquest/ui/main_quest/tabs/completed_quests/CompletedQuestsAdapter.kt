package com.stanger.listquest.ui.main_quest.tabs.completed_quests


import android.content.res.Resources
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import com.stanger.listquest.BR
import com.stanger.listquest.R
import com.stanger.listquest.data.models.MainQuestModel
import com.stanger.listquest.data.models.UserModel
import com.stanger.listquest.databinding.ItemMainQuestBinding
import com.stanger.listquest.ui.main_quest.MainQuestViewModel
import com.stanger.listquest.ui.main_quest.main_quest_item.MainQuestItemViewModel
import com.stanger.listquest.utils.FirestoreRepository


class CompletedQuestsAdapter internal constructor(options: FirestoreRecyclerOptions<MainQuestModel>,
                                                  val viewModel: MainQuestViewModel,
                                                  val resources: Resources,
                                                  val userModel: UserModel
) : FirestoreRecyclerAdapter<MainQuestModel, CompletedQuestsAdapter.MyViewHolder>(options) {

    var firebaseRepository = FirestoreRepository()

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, mainQuest: MainQuestModel) {
        holder.bind(mainQuest)
        holder.binding.chkBx.setOnClickListener{
            archiveCompletedQuest(deleteSideQuest(position, true))
        }
    }

    private fun deleteSideQuest(position: Int, boolean: Boolean): MainQuestModel {
        val snap: DocumentSnapshot = snapshots.getSnapshot(position)
        val mainQuestModel: MainQuestModel = snap.toObject(MainQuestModel::class.java)!!
        snap.reference.delete()

        if(boolean) viewModel.updateUserDelete()
        else viewModel.updateUserAdd()

        return mainQuestModel
    }

    private fun archiveCompletedQuest(mainQuestModel: MainQuestModel){
        mainQuestModel.completed = false
        firebaseRepository.addMainQuestToFirestore(mainQuestModel, userModel)
        if (mainQuestModel.favorite) firebaseRepository.addFavoriteQuestToFirestore(mainQuestModel, userModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = DataBindingUtil.inflate<ItemMainQuestBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_main_quest, parent, false
        )
        return MyViewHolder(binding)
    }

    inner class MyViewHolder internal constructor(val binding: ItemMainQuestBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(mainQuest: MainQuestModel) {
            binding.setVariable(BR.mainQuest, mainQuest)
            binding.setVariable(BR.mainViewModel,
                MainQuestItemViewModel(
                    mainQuest,
                    resources)
            )
            binding.setVariable(BR.viewModel, viewModel)
            binding.chkBx.isChecked = true
            binding.title.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }
    }
}