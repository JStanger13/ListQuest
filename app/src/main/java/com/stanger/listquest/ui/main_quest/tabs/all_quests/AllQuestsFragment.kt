package com.stanger.listquest.ui.main_quest.tabs.all_quests


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
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.stanger.listquest.BR
import com.stanger.listquest.R
import com.stanger.listquest.data.models.MainQuestModel
import com.stanger.listquest.data.models.UserModel
import com.stanger.listquest.databinding.FragmentTabAllQuestsBinding
import com.stanger.listquest.ui.main_quest.MainQuestFragment
import com.stanger.listquest.ui.main_quest.MainQuestViewModel
import com.stanger.listquest.ui.main_quest.MainQuestViewModelFactory
import com.stanger.listquest.ui.main_quest.view_pager.PageViewModel
import com.stanger.listquest.ui.main_quest.view_pager.PageViewModelFactory
import com.stanger.listquest.utils.FirestoreRepository
import com.stanger.listquest.utils.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.fragment_tab_all_quests.*


class AllQuestsFragment(val userModel: UserModel): Fragment(), View.OnClickListener{
    lateinit var binding: FragmentTabAllQuestsBinding
    var firebaseRepository = FirestoreRepository()

    private lateinit var pageViewModel: PageViewModel
    private lateinit var viewModel: MainQuestViewModel

    private var adapter: AllQuestsAdapter? = null


    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        if (adapter != null) {
            adapter!!.stopListening()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpPagerView()
    }

    fun setUpPagerView(){
        pageViewModel = ViewModelProviders.of(this,
            PageViewModelFactory(userModel)
        ).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_tab_all_quests,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.apply {}

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpViewModel()
        setUpRecyclerView()
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            MainQuestViewModelFactory(resources,
                activity!!.supportFragmentManager,
                coordinator_layout,
                MainQuestFragment(userModel),
                userModel)
        ).get(MainQuestViewModel::class.java)
        binding.setVariable(BR.viewModel, viewModel)
        binding.setVariable(BR.clickListener, this)
    }

    private fun setUpRecyclerView() {
        recycler_view.layoutManager = LinearLayoutManager(context)
        adapter = AllQuestsAdapter(
            pageViewModel.getMainQuestQuery(),
            viewModel,
            activity!!.resources,
            userModel)
        recycler_view.adapter = adapter

        val swipeHandler = object : SwipeToDeleteCallback(context!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedQuest = adapter!!.deleteSideQuest(viewHolder.adapterPosition, false)
                showUndoSnackBar(deletedQuest)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recycler_view)
        (recycler_view.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = true
    }

    fun showUndoSnackBar(mainQuestModel: MainQuestModel) {
        val snackbar = Snackbar.make(coordinator_layout, "deleted quest:  " + mainQuestModel.mainQuestTitle, Snackbar.LENGTH_LONG)
        snackbar.setAction("undo") {
            undoDelete(mainQuestModel)
        }.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                    firebaseRepository.deleteMainQuestToFirestore(mainQuestModel, userModel)
                }

            }
        })
        snackbar.show()
    }
    private fun undoDelete(mainQuestModel: MainQuestModel) {
        firebaseRepository.addMainQuestToFirestore(mainQuestModel, userModel)
        if(mainQuestModel.favorite)firebaseRepository.addFavoriteQuestToFirestore(mainQuestModel, userModel)
        viewModel.isListEmpty.value = false
        userModel.numQuestsAvail++
        firebaseRepository.updateUser(userModel)
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
//        @JvmStatic
//        fun newInstance(sectionNumber: Int): BaseFragment {
//            return BaseFragment().apply {
//                arguments = Bundle().apply {
//                    putInt(ARG_SECTION_NUMBER, sectionNumber)
//                }
//            }
//        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab -> viewModel.createMainQuest()
        }
    }
}