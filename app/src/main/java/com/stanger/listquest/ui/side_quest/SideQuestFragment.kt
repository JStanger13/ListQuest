package com.stanger.listquest.ui.side_quest


import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.snackbar.Snackbar
import com.stanger.listquest.BR
import com.stanger.listquest.R
import com.stanger.listquest.data.models.MainQuestModel
import com.stanger.listquest.data.models.SideQuestModel
import com.stanger.listquest.data.models.UserModel
import com.stanger.listquest.databinding.FragmentSideQuestBinding
import com.stanger.listquest.hideKeyboard
import com.stanger.listquest.utils.*
import kotlinx.android.synthetic.main.fragment_side_quest.*


class SideQuestFragment(val userModel: UserModel) : Fragment(),
    SideEditQuestListener,
    View.OnClickListener{
    private var adapter: SideQuestAdapter? = null
    var firebaseRepository = FirestoreRepository()
    var swipeBack = false

    private lateinit var viewModel: SideQuestViewModel

    lateinit var binding: FragmentSideQuestBinding
    lateinit var currentMainQuestModel: MainQuestModel

    fun newInstance(mainQuest: MainQuestModel): SideQuestFragment {
        val sideQuestFragment = SideQuestFragment(userModel)
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        textView3.imeOptions = EditorInfo.IME_ACTION_DONE
        notes_edit.imeOptions = EditorInfo.IME_ACTION_DONE
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)


        KeyboardEventListener(activity as AppCompatActivity) { isOpen ->
            //            if(isOpen) imageView3.visibility = View.GONE
//            else imageView3.visibility = View.VISIBLE

            if(notes_edit != null) notes_edit.isCursorVisible = isOpen
            if(textView3 != null) textView3.isCursorVisible = isOpen
            currentMainQuestModel.notes = viewModel.notes.value.toString()
            currentMainQuestModel.mainQuestTitle = viewModel.mainQuestTitle.value.toString()
            viewModel.editMaxLines.value = 1
            firebaseRepository.editMainQuest(currentMainQuestModel, userModel)
        }
    }

    private val myCallback = object: ItemTouchHelper.SimpleCallback(0,
        ItemTouchHelper.LEFT) {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            showUndoSnackBar(
                adapter!!.deleteSideQuest(viewHolder.adapterPosition)
            )
        }

        override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
            if (swipeBack) {
                swipeBack = false
                return 0
            }
            return super.convertToAbsoluteDirection(flags, layoutDirection)
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
        binding.constraintLayout.setOnClickListener {
            it.hideKeyboard()
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpViewModel()
        setUpRecyclerView()
    }

    private fun setUpViewModel() {
        viewModel = SideQuestViewModel(currentMainQuestModel,
            resources,
            activity!!.supportFragmentManager, context!!, userModel)

        binding.setVariable(BR.viewModel, viewModel)
        binding.setVariable(BR.mainQuest, currentMainQuestModel)
        binding.setVariable(BR.clickListener, this)
    }

    private fun setUpRecyclerView() {
        side_quest_recycler_view.layoutManager = LinearLayoutManager(context)
        adapter = SideQuestAdapter(
            viewModel.getSideQuestQuery(currentMainQuestModel),
            currentMainQuestModel,
            fragmentManager!!,
            this,
            activity as AppCompatActivity, userModel)

        //val myHelper = ItemTouchHelper,
        //myHelper.attachToRecyclerView(side_quest_recycler_view)
        side_quest_recycler_view.adapter = adapter

        val swipeHandler = object : SwipeToDeleteCallback(context!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val adapter = side_quest_recycler_view.adapter as SideQuestAdapter
                showUndoSnackBar(
                    adapter.deleteSideQuest(viewHolder.adapterPosition)
                )
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(side_quest_recycler_view)
        (side_quest_recycler_view.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = true
    }


//    val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
//        // Keep a reference to the last state of the keyboard
//        private var lastState: Boolean = activity.isKeyboardOpen()
//        /**
//         * Something in the layout has changed
//         * so check if the keyboard is open or closed
//         * and if the keyboard state has changed
//         * save the new state and invoke the callback
//         */
//        override fun onGlobalLayout() {
//            val isOpen = activity.isKeyboardOpen()
//            if (isOpen == lastState) {
//                return
//            } else {
//                dispatchKeyboardEvent(isOpen)
//                lastState = isOpen
//            }
//        }
//    }

    fun showUndoSnackBar(sideQuestModel: SideQuestModel) {
        val snackbar = Snackbar.make(side_coordinator_layout, sideQuestModel.sideQuestTitle, Snackbar.LENGTH_LONG)
        snackbar.setAction("undo") {
            undoDelete(sideQuestModel)
        }
        snackbar.show()
    }

    private fun undoDelete(sideQuestModel: SideQuestModel) {
        DatabaseUtils.addSideQuestToFirestore(currentMainQuestModel.id, sideQuestModel)
        firebaseRepository.updateUser(userModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setUpViewModel()
//        setUpRecyclerView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun editSideQuestFromDialog(sideQuest: SideQuestModel) {
        firebaseRepository.editSideQuest(currentMainQuestModel, sideQuest, userModel)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab -> {
                viewModel.launchCreateSideQuest()
            }
        }
    }
}