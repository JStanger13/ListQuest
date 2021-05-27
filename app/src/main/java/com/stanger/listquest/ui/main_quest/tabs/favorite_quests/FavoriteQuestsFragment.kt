package com.stanger.listquest.ui.main_quest.tabs.favorite_quests


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.stanger.listquest.BR
import com.stanger.listquest.R
import com.stanger.listquest.data.models.UserModel
import com.stanger.listquest.databinding.FragmentTabFavoriteQuestsBinding
import com.stanger.listquest.ui.main_quest.MainQuestFragment
import com.stanger.listquest.ui.main_quest.MainQuestViewModel
import com.stanger.listquest.ui.main_quest.MainQuestViewModelFactory
import com.stanger.listquest.ui.main_quest.view_pager.PageViewModel
import com.stanger.listquest.ui.main_quest.view_pager.PageViewModelFactory
import kotlinx.android.synthetic.main.fragment_tab_favorite_quests.*

class FavoriteQuestsFragment(val userModel: UserModel): Fragment(){
    lateinit var binding: FragmentTabFavoriteQuestsBinding

    private lateinit var pageViewModel: PageViewModel
    private lateinit var viewModel: MainQuestViewModel

    private var adapter: FavoriteQuestsAdapter? = null

    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        if (adapter != null) {
            //adapter!!.stopListening()
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
            R.layout.fragment_tab_favorite_quests,
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
                coordinator_layout3,
                MainQuestFragment(userModel),
                userModel
            )
        ).get(MainQuestViewModel::class.java)
        binding.setVariable(BR.viewModel, viewModel)
    }

    private fun setUpRecyclerView() {
        recycler_view3.layoutManager = LinearLayoutManager(context)
        adapter = FavoriteQuestsAdapter(
            pageViewModel.getFavoritesQuestsQuery(),
            viewModel,
            resources,
            userModel
        )
        recycler_view3.adapter = adapter
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

    }
}