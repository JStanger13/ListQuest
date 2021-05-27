package com.stanger.listquest.ui.main_quest.view_pager

import android.content.Context
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.stanger.listquest.R
import com.stanger.listquest.data.models.UserModel
import com.stanger.listquest.ui.main_quest.tabs.all_quests.AllQuestsFragment
import com.stanger.listquest.ui.main_quest.tabs.completed_quests.CompletedQuestsFragment


private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
//    R.string.tab_text_2,
    R.string.tab_text_3
)


class SectionsPagerAdapter(private val context: Context,
                           fm: FragmentManager,
                           val userModel: UserModel
)
    : FragmentPagerAdapter(fm) {

    private val fragList = arrayOf(
        AllQuestsFragment(userModel),
//        FavoriteQuestsFragment(userModel),
        CompletedQuestsFragment(userModel)
    )

    override fun getItem(position: Int): Fragment {


        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return fragList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    fun onTouchEvent(event: MotionEvent?): Boolean {
        return false
    }

    fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        return false
    }

    override fun getCount(): Int {
        return fragList.size
    }
}