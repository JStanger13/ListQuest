package com.example.listquest.ui.mainquest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.listquest.BR
import com.example.listquest.R
import com.example.listquest.data.models.MainQuestModel
import com.example.listquest.data.utils.FirestoreRepository
import com.example.listquest.data.utils.FirestoreUtil.userModel
import com.example.listquest.data.utils.SwipeToDeleteCallback
import com.example.listquest.databinding.FragmentMainQuestBinding
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_main_quest.*
import kotlinx.android.synthetic.main.main_profile_bar.*


class MainQuestFragment : Fragment() {
    private var adapter: MainQuestAdapter? = null
    private lateinit var viewModel: MainQuestViewModel
    lateinit var binding: FragmentMainQuestBinding
    var firebaseRepository = FirestoreRepository()

    companion object {
        fun newInstance() = MainQuestFragment()
    }

    override fun onStart() {
        super.onStart()
        Picasso.get().load(userModel.profilePicturePath).into(user_img)
        adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        if (adapter != null) {
            adapter!!.stopListening()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main_quest,
            container,
            false
        )

        binding.lifecycleOwner = this
        binding.apply { }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUserInfo()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpViewModel()
        setUpRecyclerView()
        viewModel.getNumOfAvailableMainQuests()
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            MainQuestViewModelFactory(resources,
                activity!!.supportFragmentManager)
        ).get(MainQuestViewModel::class.java)
        binding.setVariable(BR.viewModel, viewModel)
    }

    private fun setUpRecyclerView() {
        recycler_view.layoutManager = LinearLayoutManager(context)
        adapter = MainQuestAdapter(
            viewModel.getMainQuestQuery(),
            viewModel,
            activity!!.resources
        )
        recycler_view.adapter = adapter

        val swipeHandler = object : SwipeToDeleteCallback(context!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val adapter = recycler_view.adapter as MainQuestAdapter
                showUndoSnackBar(
                    adapter.deleteSideQuest(viewHolder.adapterPosition)
                )
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recycler_view)
        (recycler_view.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = true
    }

    fun showUndoSnackBar(mainQuestModel: MainQuestModel) {
        val snackbar = Snackbar.make(coordinator_layout, mainQuestModel.mainQuestTitle, Snackbar.LENGTH_LONG)
        snackbar.setAction("undo") {
            undoDelete(mainQuestModel)
        }
        snackbar.show()
    }

    private fun undoDelete(mainQuestModel: MainQuestModel) {
        firebaseRepository.addMainQuestToFirestore(mainQuestModel)
        viewModel.isListEmpty.value = false
        userModel.numQuestsAvail++
        firebaseRepository.updateUser(userModel)
    }
}