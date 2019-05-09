package com.example.listquest.ui.sidequest

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listquest.R
import com.example.listquest.databinding.ItemSideQuestBinding
import com.example.listquest.models.MainQuestModel
import com.example.listquest.utils.KeyboardUtils
import kotlinx.android.synthetic.main.fragment_side_quest.*
import kotlinx.android.synthetic.main.fragment_side_quest.view.*
import kotlinx.android.synthetic.main.item_side_quest.view.*
import kotlin.math.roundToInt


class SideQuestFragment: Fragment() {
    lateinit var mainQuest: MainQuestModel
    private var shortAnimationDuration: Int = 0
    var itemsChecked: Float = 0F
    fun setHealthBar(view: View){
        view.findViewById<ProgressBar>(R.id.health_bar).progress = setHealth()
    }
    fun setHealth(): Int {
        return if (itemsChecked == 0F) {
            0
        } else if (mainQuest.bossHealth > 0){
            return mainQuest.bossHealth
        }else{
            ((itemsChecked/mainQuest.sideQuestList.size.toFloat())*100).roundToInt()
        }
    }

    companion object {
        fun newInstance() = SideQuestFragment()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(com.example.listquest.R.layout.fragment_side_quest, container, false)

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sharedElementEnterTransition = TransitionInflater
                .from(context).inflateTransition(
                    android.R.transition.explode // you can change this
                )
        }
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = this.arguments;
        if (bundle != null) {
            mainQuest = bundle.getSerializable("x") as MainQuestModel
            view.findViewById<TextView>(R.id.main_quest_title).text = mainQuest.mainQuestTitle
            view.findViewById<ProgressBar>(R.id.health_bar).progress = mainQuest.bossHealth
        }

        var sideQuestViewModel = ViewModelProviders.of(this).get(SideQuestViewModel::class.java)
        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
        var sideAdapter = SideQuestRecyclerView(view)

        side_quest_recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = sideAdapter
        }

        view.side_edit.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                KeyboardUtils.hideSoftKeyBoard(v.context, v)
                side_quest_recycler_view.visibility = View.VISIBLE
                sideQuestViewModel.addQuestItem(mainQuest, side_edit.text.toString(), sideAdapter)
                setHealthBar(view)
                //Toast.makeText(v.context, setHealth().toString(), Toast.LENGTH_LONG).show()
                Toast.makeText(v.context, mainQuest.bossHealth.toString(), Toast.LENGTH_LONG).show()

                view.side_edit.setText("")
                true
            } else {
                false
            }
        }
    }

    inner class SideQuestRecyclerView(view: View) : RecyclerView.Adapter<SideQuestFragment.SideQuestViewHolder>() {
        private val v = view
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SideQuestFragment.SideQuestViewHolder {
            val binding = DataBindingUtil.inflate<com.example.listquest.databinding.ItemSideQuestBinding>(
                LayoutInflater.from(parent.context),
                com.example.listquest.R.layout.item_side_quest, parent, false
            )
            return SideQuestViewHolder(binding)
        }

        private fun removeItem(position: Int) {
            mainQuest.sideQuestList.removeAt(position)
            notifyDataSetChanged()
        }

        override fun getItemCount() = mainQuest.sideQuestList.size

        override fun onBindViewHolder(holder: SideQuestViewHolder, position: Int) {
            val dataModel = mainQuest.sideQuestList[position]
            holder.binding.sideModel = dataModel
            holder.bind(dataModel)
            holder.binding.itemCardview2.side_delete.setOnClickListener{ removeItem(position) }

            holder.binding.itemCardview2.chk.isChecked = dataModel.isChecked

            holder.binding.itemCardview2.chk.setOnClickListener{
                if (holder.binding.itemCardview2.chk.isChecked) {
                    holder.binding.itemCardview2.txt.setTextColor(0xffff0000.toInt())
                    dataModel.isChecked = true
                    itemsChecked++
                }else{
                    holder.binding.itemCardview2.txt.setTextColor(0xFFFFFFFF.toInt())
                    dataModel.isChecked = false
                    itemsChecked--
                }
                setHealthBar(v)
                mainQuest.bossHealth = setHealth()
                Toast.makeText(v.context, setHealth().toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    class SideQuestViewHolder(var binding: ItemSideQuestBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(obj: Any) {
            binding.setVariable(BR.model, obj)
            binding.executePendingBindings()
        }
    }
}