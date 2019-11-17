package com.example.listquest.data.utils

import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentManager
import com.example.listquest.R
import com.example.listquest.data.models.MainQuestModel
import com.example.listquest.ui.dialogs.*
import com.example.listquest.ui.dialogs.calendar.CalenderFragment
import com.example.listquest.ui.dialogs.level_up.LevelUpDialogFragment
import com.example.listquest.ui.mainquest.create_main_quest.CreateMainDialogFragment
import com.example.listquest.ui.mainquest.main_quest_more.MainQuestMoreDialog
import com.example.listquest.ui.sidequest.SideQuestFragment
import com.example.listquest.ui.sidequest.create_side_quest.CreateSideDialogFragment

class DialogUtils{
    companion object {
        lateinit var imm: InputMethodManager

        fun launchMainMoreDialog(fm: FragmentManager, frag: MainQuestMoreDialog){
            frag.show(fm, "main_quest_more")
        }

        fun launchLvlUp(fm: FragmentManager, frag: LevelUpDialogFragment){
            frag.show(fm, "fragment_lvl_up")
        }

        fun launchCalendarDialog(fm: FragmentManager, frag: CalenderFragment ){
            frag.show(fm, "fragment_calender")
        }

        fun launchCreateMainQuestDialog(fm: FragmentManager, frag: CreateMainDialogFragment){
            frag.show(fm, "fragment_create_main")
        }

        fun launchEditMainDialog(fm: FragmentManager, frag: EditDialogFragment) {
            frag.show(fm, "fragment_edit_mainquest")
        }

        fun launchSideQuestFragment(fm: FragmentManager, frag: SideQuestFragment, mainQuest: MainQuestModel){
            fm.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left)
                .replace(R.id.container, frag.newInstance(mainQuest), "SideQuestId")
                .addToBackStack(null)
            .commit()
        }

        fun launchMainQuestFragment(fm: FragmentManager){
            fm.popBackStack()
        }

        fun launchCreateSideQuestDialog(fm: FragmentManager, frag: CreateSideDialogFragment){
            frag.show(fm, "fragment_create_sidequest")
        }
    }
}