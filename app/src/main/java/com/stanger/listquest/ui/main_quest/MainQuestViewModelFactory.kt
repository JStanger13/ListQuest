package com.stanger.listquest.ui.main_quest


import android.content.res.Resources
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stanger.listquest.data.models.UserModel

class MainQuestViewModelFactory (private val res: Resources,
                                 val  fm: FragmentManager,
                                 val cl: CoordinatorLayout,
                                 val frag: MainQuestFragment,
                                 val userModel: UserModel
): ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create(MainQuestViewModel:Class<T>): T {
        return MainQuestViewModel(res, fm, cl, frag, userModel) as T
    }
}
