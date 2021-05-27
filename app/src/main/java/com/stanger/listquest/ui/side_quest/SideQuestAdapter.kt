package com.stanger.listquest.ui.side_quest


import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import com.stanger.listquest.BR
import com.stanger.listquest.R
import com.stanger.listquest.data.models.MainQuestModel
import com.stanger.listquest.data.models.SideQuestModel
import com.stanger.listquest.data.models.UserModel
import com.stanger.listquest.databinding.ItemSideQuestBinding
import com.stanger.listquest.ui.side_quest.side_quest_item.SideQuestItemViewModel
import com.stanger.listquest.utils.SideEditQuestListener


class SideQuestAdapter internal constructor(
    options: FirestoreRecyclerOptions<SideQuestModel>,
    val mainQuest: MainQuestModel,
    val fm: FragmentManager,
    val callback: SideEditQuestListener,
    val activity: AppCompatActivity,
    val userModel: UserModel
):
    FirestoreRecyclerAdapter<SideQuestModel, SideQuestAdapter.MyViewHolder>(options){


    override fun onBindViewHolder(holder: MyViewHolder, poition: Int, sideQuest: SideQuestModel) {
        holder.bind(sideQuest)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_side_quest
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding = DataBindingUtil.inflate<ItemSideQuestBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_side_quest, parent, false)

        return MyViewHolder(binding)
    }

    fun deleteSideQuest(position: Int): SideQuestModel{
        val snap: DocumentSnapshot = snapshots.getSnapshot(position)
        val sideQuestModel = snap.toObject(SideQuestModel::class.java)!!
        snapshots.getSnapshot(position).reference.delete()
        return sideQuestModel
    }

    inner class MyViewHolder internal constructor(val binding: ItemSideQuestBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(sideQuest: SideQuestModel) {
            val viewModel = SideQuestItemViewModel(
                mainQuest,
                fm,
                sideQuest,
                callback,
                userModel
            )

            binding.setVariable(BR.sideQuest, sideQuest)
            binding.setVariable(BR.sideViewModel, viewModel)
            binding.executePendingBindings()
            if(sideQuest.isChecked) binding.txt.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            else binding.txt.paintFlags = 0

        }
    }
}