package com.example.listquest.ui.mainquest

import android.content.DialogInterface
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listquest.databinding.ItemMainQuestBinding
import com.example.listquest.models.MainQuestModel
import com.example.listquest.ui.mainquest.MainQuestViewModel.Companion.mainQuestList
import com.example.listquest.ui.sidequest.SideQuestFragment
import com.example.listquest.utils.KeyboardUtils
import kotlinx.android.synthetic.main.fragment_main_quest.*
import kotlinx.android.synthetic.main.fragment_main_quest.view.*
import kotlinx.android.synthetic.main.item_main_quest.view.*
import com.example.listquest.ui.dialogs.EditDialogFragment


class MainQuestFragment : Fragment() {

    private var tapPosition = DiffUtil.DiffResult.NO_POSITION
    val viewRect = Rect()
    var mainAdapter = MainQuestRecyclerView()
    private var shortAnimationDuration: Int = 0

    companion object {
        fun newInstance() = MainQuestFragment()
        lateinit var viewModel: MainQuestViewModel
    }

    fun getQuestList(): MutableList<MainQuestModel>{
        return viewModel.getQuestList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(com.example.listquest.R.layout.fragment_main_quest, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
        viewModel = ViewModelProviders.of(this).get(MainQuestViewModel::class.java)
        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = mainAdapter
        }

        view.edit_text.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                KeyboardUtils.hideSoftKeyBoard(v.context, v)
                viewModel.addQuestItem(edit_text.text.toString(), mainAdapter)
                view.edit_text.setText("")
                true
            } else {
                false
            }
        }
    }

    inner class MainQuestRecyclerView : RecyclerView.Adapter<MainQuestViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainQuestViewHolder {
            val binding = DataBindingUtil.inflate<com.example.listquest.databinding.ItemMainQuestBinding>(
                LayoutInflater.from(parent.context),
                com.example.listquest.R.layout.item_main_quest, parent, false
            )
            return MainQuestViewHolder(binding)
        }

        private fun removeItem(position: Int) {
            mainQuestList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, mainQuestList.size)
        }

        override fun getItemCount() = mainQuestList.size

        override fun onBindViewHolder(holder: MainQuestViewHolder, position: Int) {
            holder.binding.itemClickListener = BadgeBind()

            fun onViewClick() {
                tapPosition = position
                holder.itemView.getGlobalVisibleRect(viewRect)
                val fragment = SideQuestFragment()
                val bundle = Bundle()
                bundle.putSerializable("x", mainQuestList[position])
                fragment.arguments = bundle

                activity!!.supportFragmentManager
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(com.example.listquest.R.id.container, fragment)
                    .addToBackStack(null)
                    .addSharedElement(holder.itemView, getString(com.example.listquest.R.string.transition_name))
                    .commit()
            }
            holder.bind(mainQuestList[position], ::onViewClick)
            holder.binding.itemCardview.delete.setOnClickListener{ removeItem(position) }
            holder.binding.itemCardview.edit.setOnClickListener {
                val fm = activity?.supportFragmentManager
                val editNameDialogFragment = EditDialogFragment().newInstance(position)
                editNameDialogFragment.show(fm, "fragment_edit_name")
            }
        }
    }

class MainQuestViewHolder(var binding: ItemMainQuestBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(obj: Any, expandHandler: () -> Unit) {
        binding.setVariable(BR.model, obj)
        binding.executePendingBindings()
        binding.itemCardview.setOnClickListener { expandHandler() }
        }
    }
}

class BadgeBind{
    fun badgeOnClick(view: View){

        val dialogBuilder = AlertDialog.Builder(view.context)

        // set message of alert dialog
        dialogBuilder.setMessage("Do you want to close this application ?")
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Proceed", DialogInterface.OnClickListener {
                    dialog, id -> dialog.dismiss()
            })
            // negative button text and action
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        alert.window.setLayout(600, 400)

        // set title for alert dialog box
        alert.setTitle("AlertDialogExample")
        // show alert dialog
        alert.show()
    }
}



