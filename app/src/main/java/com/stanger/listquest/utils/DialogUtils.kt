package com.stanger.listquest.utils


import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentManager
import com.stanger.listquest.MainActivity
import com.stanger.listquest.R
import com.stanger.listquest.data.models.MainQuestModel
import com.stanger.listquest.data.models.UserModel
import com.stanger.listquest.ui.calendar.CalenderFragment
import com.stanger.listquest.ui.delete_account.DeleteAccountFragment
import com.stanger.listquest.ui.forgot_password.ForgotPasswordFragment
import com.stanger.listquest.ui.login.LoginFragment
import com.stanger.listquest.ui.main_quest.MainQuestFragment
import com.stanger.listquest.ui.main_quest.create_main_quest_dialog.CreateMainQuestDialogFragment
import com.stanger.listquest.ui.profile.ProfileFragment
import com.stanger.listquest.ui.profile.photo_dialog.PhotoDialogFragment
import com.stanger.listquest.ui.side_quest.SideQuestFragment
import com.stanger.listquest.ui.side_quest.create_side_quest_dialog.CreateSideQuestDialogFragment
import com.stanger.listquest.ui.side_quest.edit_side_quest_dialog.EditSideQuestDialogFragment
import com.stanger.listquest.ui.sign_up.SignUpFragment

class DialogUtils {
    companion object {
        lateinit var imm: InputMethodManager

//        fun launchSettingsFragment(
//            fm: FragmentManager,
//            frag: SettingsFragment
//        ) {
//            fm.beginTransaction()
//                .setCustomAnimations(
//                    R.anim.slide_up,
//                    R.anim.slide_down,
//                    R.anim.slide_up,
//                    R.anim.slide_down
//                )
//                .add(R.id.container, frag, "SettingsFragment")
//                .addToBackStack(null)
//                .commit()
//        }


        fun launchProfileFragment(fm: FragmentManager, frag: ProfileFragment) {
            fm.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_up,
                    R.anim.slide_down,
                    R.anim.slide_up,
                    R.anim.slide_down
                )
                .add(R.id.container, frag, "ProfieFragment")
                .addToBackStack(null)
                .commit()
        }

        fun launchDeleteDialog(fm: FragmentManager, frag: DeleteAccountFragment){
            fm.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_up,
                    R.anim.slide_down,
                    R.anim.slide_up,
                    R.anim.slide_down
                )
                .add(R.id.container, frag, "ProfieFragment")
                .addToBackStack(null)
                .commit()
        }

        fun launchPhotoDialog(fm: FragmentManager, frag: PhotoDialogFragment){
            frag.show(fm, "photo_dialog")
        }

        fun launchCalendarDialog(fm: FragmentManager, frag: CalenderFragment) {
            frag.show(fm, "fragment_calender")
        }

        fun launchCreateMainQuestDialog(fm: FragmentManager, frag: CreateMainQuestDialogFragment) {
            frag.show(fm, "fragment_create_main")
        }

        fun launchEditSideDialog(fm: FragmentManager, frag: EditSideQuestDialogFragment) {
            frag.show(fm, "fragment_edit_mainquest")
        }

        fun launchSideQuestFragment(
            fm: FragmentManager,
            frag: SideQuestFragment,
            mainQuest: MainQuestModel
        ) {
            fm.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_up,
                    R.anim.slide_down,
                    R.anim.slide_up,
                    R.anim.slide_down
                )
                .add(R.id.container, frag.newInstance(mainQuest), "SideQuestId")
                .addToBackStack(null)
                .commit()
        }

        fun launchLogInFragment(fm: FragmentManager){
            fm.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_up,
                    R.anim.slide_down,
                    R.anim.slide_up,
                    R.anim.slide_down
                )
                .replace(R.id.container, MainActivity.loginFragment, "")
                .commitNow()
        }

        fun launchForgotPasswordFragment(fm: FragmentManager, frag: ForgotPasswordFragment){
            fm.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_up,
                    R.anim.slide_down,
                    R.anim.slide_up,
                    R.anim.slide_down
                )
                .add(R.id.container, frag, "")
                .addToBackStack(null)
                .commit()
        }

        fun launchCreateAccountFragment(fm: FragmentManager, frag: SignUpFragment){
            fm.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_up,
                    R.anim.slide_down,
                    R.anim.slide_up,
                    R.anim.slide_down
                )
                .replace(R.id.container, frag, "")
                .commit()
        }

        fun launchLoginFragment(fm: FragmentManager, frag: LoginFragment){
            fm.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_up,
                    R.anim.slide_down,
                    R.anim.slide_up,
                    R.anim.slide_down
                )
                .replace(R.id.container, frag, "")
                .commit()
        }
        fun launchMainQuestFromLogin(fm: FragmentManager, userModel: UserModel){
            fm.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_up,
                    R.anim.slide_down,
                    R.anim.slide_up,
                    R.anim.slide_down
                )
                .replace(R.id.container, MainQuestFragment(userModel), "")
                .commit()
        }

        fun launchMainQuestFragment(fm: FragmentManager) {
            fm.popBackStack()
        }

        fun launchCreateSideQuestDialog(fm: FragmentManager, frag: CreateSideQuestDialogFragment) {
            frag.show(fm, "fragment_create_sidequest")
        }
    }
}