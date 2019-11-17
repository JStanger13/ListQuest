package com.example.listquest.ui.sidequest

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listquest.BR
import com.example.listquest.R
import com.example.listquest.data.models.MainQuestModel
import com.example.listquest.data.utils.FirestoreRepository
import com.example.listquest.databinding.FragmentSideQuestBinding
import com.example.listquest.ui.sidequest.item_viewmodel.SideQuestViewModel
import com.example.listquest.ui.sidequest.item_viewmodel.SideQuestViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_side_quest.*


class SideQuestFragment : Fragment(){
    private var adapter: SideQuestAdapter? = null
    var firebaseRepository = FirestoreRepository()
    var swipeBack = false

    private lateinit var viewModel: SideQuestViewModel

    lateinit var binding: FragmentSideQuestBinding
    lateinit var currentMainQuestModel: MainQuestModel

    fun newInstance(mainQuest: MainQuestModel): SideQuestFragment {
        val sideQuestFragment = SideQuestFragment()
        val args = Bundle()
        args.putSerializable("MainQuestModel", mainQuest)
        sideQuestFragment.arguments = args
        return sideQuestFragment
    }

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

    override fun onResume() {
        super.onResume()
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    private val myCallback = object: ItemTouchHelper.SimpleCallback(0,
        ItemTouchHelper.LEFT) {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            //deletedSideQuestModel = adapter!!.deleteSideQuest(viewHolder.adapterPosition)
            adapter!!.deleteSideQuest(viewHolder.adapterPosition)
            val snack = Snackbar.make(viewHolder.itemView,"This is a simple Snackbar",Snackbar.LENGTH_LONG)
            snack.show()
        }

        override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
            if (swipeBack) {
                swipeBack = false
                return 0
            }
            return super.convertToAbsoluteDirection(flags, layoutDirection)
        }

        // SwipeController.java
        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float, dY: Float,
            actionState: Int, isCurrentlyActive: Boolean
        ) {

            if (actionState == ACTION_STATE_SWIPE) {
                setTouchListener(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }

        @SuppressLint("ClickableViewAccessibility")
        private fun setTouchListener(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float, dY: Float,
            actionState: Int, isCurrentlyActive: Boolean
        ) {

            recyclerView.setOnTouchListener { v, event ->
                swipeBack =
                    event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP
                false
            }
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        currentMainQuestModel = arguments!!.getSerializable("MainQuestModel") as MainQuestModel
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_side_quest, container, false)
        binding.lifecycleOwner = this

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
            SideQuestViewModelFactory(
                currentMainQuestModel,
                resources,
                activity!!.supportFragmentManager, context!!
            )
        ).get(SideQuestViewModel::class.java)
        binding.setVariable(BR.viewModel, viewModel)
    }

    private fun setUpRecyclerView() {
        side_quest_recycler_view.layoutManager = LinearLayoutManager(context)
        adapter = SideQuestAdapter(
            viewModel.getSideQuestQuery(currentMainQuestModel),
            currentMainQuestModel
        )
        side_quest_recycler_view.adapter = adapter

        val myHelper = ItemTouchHelper(myCallback)
        myHelper.attachToRecyclerView(side_quest_recycler_view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel()
        setUpRecyclerView()
    }
}