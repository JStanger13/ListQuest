package com.example.listquest.ui.mainquest

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.listquest.BR
import com.example.listquest.R
import com.example.listquest.data.models.MainQuestModel
import com.example.listquest.data.models.NotesModel
import com.example.listquest.data.utils.FirestoreRepository
import com.example.listquest.data.utils.FirestoreUtil.userModel
import com.example.listquest.databinding.ItemMainQuestBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot

class MainQuestAdapter internal constructor(options: FirestoreRecyclerOptions<MainQuestModel>,
                                            val viewModel: MainQuestViewModel,
                                            val resources: Resources) :
    FirestoreRecyclerAdapter<MainQuestModel, MainQuestAdapter.MyViewHolder>(options) {

    //lateinit var listener: OnItemClickListener

    var firebaseRepository = FirestoreRepository()

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, mainQuest: MainQuestModel) {
        holder.bind(mainQuest)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = DataBindingUtil.inflate<ItemMainQuestBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_main_quest, parent, false)
        return MyViewHolder(binding)
    }

    fun deleteSideQuest(position: Int): MainQuestModel{
        var snap: DocumentSnapshot = snapshots.getSnapshot(position)
        var mainQuestModel: MainQuestModel = snap.toObject(MainQuestModel::class.java)!!

        snapshots.getSnapshot(position).reference.delete()
        userModel.numQuestsAvail--
        firebaseRepository.updateUser(userModel)
        viewModel.getUserInfo()

        return mainQuestModel
    }

    inner class MyViewHolder internal constructor(val binding: ItemMainQuestBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(mainQuest: MainQuestModel) {
            binding.setVariable(BR.mainQuest, mainQuest)
            binding.setVariable(BR.viewModel, viewModel)
        }

    }


//    interface OnItemClickListener {
//        fun onItemClick(documentSnapshot: DocumentSnapshot, position: Int)
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener){
//        this.listener = listener
//    }

}



//class MainQuestAdapter internal constructor(var list: MutableList<MainQuestModel>,
//                                            val viewModel: MainQuestViewModel,
//                                            val resources: Resources) :
//    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//    var firebaseRepository = FirestoreRepository()
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        val binding = DataBindingUtil.inflate<ItemMainQuestBinding>(
//            LayoutInflater.from(parent.context),
//            R.layout.item_main_quest, parent, false
//        )
//        return MyViewHolder(binding)
//    }
//
//    override fun getItemCount(): Int {
//        return list.size
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        (holder as MyViewHolder).bind(list[position])
//    }
//
//    fun deleteSideQuest(position: Int): MainQuestModel{
//        var mainQuestModel = list[position]
//        firebaseRepository.deleteMainQuestToFirestore(mainQuestModel)
//
//        list.removeAt(position)
//        //snapshots.getSnapshot(position).reference.delete()
//        userModel.numQuestsAvail--
//        firebaseRepository.updateUser(userModel)
//        viewModel.getUserInfo()
//
//        return mainQuestModel
//    }
//
//
//    inner class MyViewHolder internal constructor(val binding: ItemMainQuestBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(mainQuest: MainQuestModel) {
//            binding.setVariable(BR.mainQuest, mainQuest)
//            binding.setVariable(BR.viewModel, viewModel)
//        }
//
//    }
//}

