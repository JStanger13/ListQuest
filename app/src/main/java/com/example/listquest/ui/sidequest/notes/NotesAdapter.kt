package com.example.listquest.ui.sidequest.bottomsheet

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.listquest.BR
import com.example.listquest.R
import com.example.listquest.databinding.ItemNotesBinding
import com.example.listquest.models.MainQuestModel
import com.example.listquest.models.NotesModel
import kotlinx.android.synthetic.main.item_notes.view.*

class NotesAdapter(var list: MutableList<NotesModel>,
                   val mainQuest: MainQuestModel,
                   val notesViewModel: NotesBottomSheetViewModel,
                   val fm: FragmentManager,
                   val frag: Fragment
): RecyclerView.Adapter<NotesAdapter.MyViewHolder>(){

    override fun getItemCount(): Int {
        return list.size + 1
    }

    @SuppressLint("RestrictedApi")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (position == 0) {
            holder.binding.itemNotesBtn.visibility = View.VISIBLE
            holder.binding.moreBtn.more_btn.visibility = View.GONE

            holder.bindBtn()
        } else {
            holder.bind(list[position - 1])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = DataBindingUtil.inflate<ItemNotesBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_notes, parent, false)

        return MyViewHolder(binding)
    }

    inner class MyViewHolder internal constructor(val binding: ItemNotesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(notes: NotesModel) {
            binding.setVariable(BR.note, notes)
            binding.setVariable(BR.noteViewModel, notesViewModel)
            binding.setVariable(BR.noteItemViewModel, getItemViewModel(notes))
            binding.executePendingBindings()
        }

        fun bindBtn(){
            binding.setVariable(BR.noteViewModel, notesViewModel)
            binding.executePendingBindings()

        }

        fun getItemViewModel(notes: NotesModel): NotesItemViewModel{
            return ViewModelProviders.of(
                frag,
                (NotesItemViewModelFactory(
                notes,
                fm)
                )
            ).get(NotesItemViewModel::class.java)
        }
    }
}

