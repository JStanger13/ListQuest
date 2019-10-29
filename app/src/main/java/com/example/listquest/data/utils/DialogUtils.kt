package com.example.listquest.utils

import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentManager
import com.example.listquest.R
import com.example.listquest.data.models.MainQuestModel
import com.example.listquest.ui.dialogs.*
import com.example.listquest.ui.sidequest.SideQuestFragment

class DialogUtils{
    companion object {
        lateinit var imm: InputMethodManager

        fun launchCreateMainQuestDialog(fm: FragmentManager, frag: CreateMainDialogFragment){
            frag.show(fm, "fragment_create_main")
        }

        fun launchEditMainDialog(fm: FragmentManager, frag: EditDialogFragment) {
            frag.show(fm, "fragment_edit_mainquest")
        }

        fun launchEditSideQuest(fm: FragmentManager, frag: EditSideDialogFragment){
            frag.show(fm, "fragment_edit_mainquest")

        }

        fun launchEditNotesDialog(fm: FragmentManager, frag: EditNotesDialogFragment){
            frag.show(fm, "fragment_edit_notes")
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

        fun launchCompleteDialog(fm: FragmentManager, frag: MissionCompleteDialogFragment) {
            frag.show(fm, "fragment_complete")
        }

        fun launchNotesDialog(fm: FragmentManager, frag: NotesDialogFragment){
            frag.show(fm, "fragment_notes")
        }
    }
}