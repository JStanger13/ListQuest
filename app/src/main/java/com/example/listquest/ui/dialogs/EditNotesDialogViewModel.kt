package com.example.listquest.ui.dialogs

import android.app.Dialog
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import com.example.listquest.data.models.MainQuestModel
import com.example.listquest.data.models.NotesModel
import com.example.listquest.data.utils.*

class EditNotesDialogViewModel(var notes: NotesModel,
                               val fm: FragmentManager,
                               val dialog: Dialog,
                               val mainQuest: MainQuestModel,
                               var callback: EditNotesDialogFragment.EditNotesListener
    ) : ViewModel() {



    var firebaseRepository = FirestoreRepository()

    fun deleteNote(){
        callback.editNotesFromDialog(mainQuest.id, notes.id)
        //irebaseRepository.deleteNoteFromFirestore(mainQuest.id, notes.id)
        dialog.dismiss()
    }

//    var bossImg = MutableLiveData<Drawable>()
//    var bossTitle = MutableLiveData<String>()
//    var newName = ""
//    var newBossName = BossUtils.getBossName(boss)
//
//
//    init {
//        bossImg.value = res.getDrawable(BossUtils.changeBossImage(boss), null)
//        bossTitle.value = BossUtils.getBossName(boss)
//    }

//    fun createButton(){
//        if(newName.isNotEmpty()) {
//            callback.addMainQuestFromDialog(newName, boss, newBossName)
//            dialog.dismiss()
//            KeyboardUtils.hideSoftKeyBoard(dialog.findViewById(R.id.edit_text_name))
//        }
//    }


}