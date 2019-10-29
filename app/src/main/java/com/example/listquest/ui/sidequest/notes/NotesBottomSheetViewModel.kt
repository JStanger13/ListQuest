package com.example.listquest.ui.sidequest.bottomsheet

import android.content.ContentValues.TAG
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.listquest.models.MainQuestModel
import com.example.listquest.models.NotesModel
import com.example.listquest.ui.dialogs.EditNotesDialogFragment
import com.example.listquest.ui.dialogs.NotesDialogFragment
import com.example.listquest.utils.DatabaseUtils.Companion.firebaseRepository
import com.example.listquest.utils.DialogUtils
import com.firebase.ui.firestore.FirestoreRecyclerOptions


class NotesBottomSheetViewModel(var currentMainQuestModel: MainQuestModel,
                                var fm: FragmentManager): ViewModel(),
    NotesDialogFragment.NotesListener,
    EditNotesDialogFragment.EditNotesListener{

    var numOfNotes =  MutableLiveData<String>()

    init {
        getNumOfNotes()
    }

    override fun editNotesDialog(notes: NotesModel) {
        firebaseRepository.addNotesToMainQuest(currentMainQuestModel.id, notes, firebaseRepository.getNotesId(currentMainQuestModel.id))
        getNumOfNotes()
    }

    override fun editNotesFromDialog(notes: NotesModel) {
        firebaseRepository.addNotesToMainQuest(currentMainQuestModel.id, notes, notes.id)
        getNumOfNotes()
    }

    fun launchNotesDialog(){
        DialogUtils.launchNotesDialog(fm, NotesDialogFragment(this).newInstance(currentMainQuestModel.notes))
    }

    fun getNotesQuery(mainQuestModel: MainQuestModel): FirestoreRecyclerOptions<NotesModel> {
        return FirestoreRecyclerOptions.Builder<NotesModel>()
            .setQuery(firebaseRepository.getNotesQuery(mainQuestModel.id), NotesModel::class.java).build()
    }

    private fun getNumOfNotes() {
        firebaseRepository.getNotesQuery(currentMainQuestModel.id)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                } else {
                    numOfNotes.value = "You have " + snapshots!!.size().toString() + " Notes"
                }
            }
    }

}