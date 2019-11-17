package com.example.listquest.ui.sidequest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.listquest.BR
import com.example.listquest.R
import com.example.listquest.databinding.ItemSideQuestBinding
import com.example.listquest.data.models.MainQuestModel
import com.example.listquest.data.models.SideQuestModel
import com.example.listquest.ui.sidequest.item_viewmodel.SideQuestItemViewModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class SideQuestAdapter internal constructor(
    options:  FirestoreRecyclerOptions<SideQuestModel>,
    val mainQuest: MainQuestModel):
    FirestoreRecyclerAdapter<SideQuestModel, SideQuestAdapter.MyViewHolder>(options) {

    override fun onBindViewHolder(holder: MyViewHolder, poition: Int, sideQuest: SideQuestModel) {
        holder.bind(sideQuest)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = DataBindingUtil.inflate<ItemSideQuestBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_side_quest, parent, false)

        return MyViewHolder(binding)
    }

    fun deleteSideQuest(position: Int){
        //val deletedSideQuestModel = snapshots.getSnapshot(position).reference as SideQuestModel
        snapshots.getSnapshot(position).reference.delete()
        //return deletedSideQuestModel
    }

    inner class MyViewHolder internal constructor(val binding: ItemSideQuestBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(sideQuest: SideQuestModel) {
            binding.setVariable(BR.sideQuest, sideQuest)
            binding.setVariable(BR.sideViewModel,
                SideQuestItemViewModel(
                    mainQuest,
                    sideQuest
                )
            )
            binding.executePendingBindings()
        }
    }
}