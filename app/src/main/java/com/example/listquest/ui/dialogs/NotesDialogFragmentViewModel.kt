package com.example.listquest.ui.dialogs

import android.app.Dialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.listquest.data.models.NotesModel
import com.example.listquest.data.utils.DatabaseUtils.Companion.firebaseRepository
import com.google.firebase.Timestamp

class NotesDialogFragmentViewModel(val id: String,
                                   val callback: NotesDialogFragment.NotesListener,
                                   val dialog: Dialog,
                                   var title: String,
                                   var body: String
) : ViewModel() {

    var notesTitleText = MutableLiveData<String>()
    var notesBodyText = MutableLiveData<String>()

    init{
        notesTitleText.value = title
        notesBodyText.value = body
    }

    fun createButton() {
        if (!notesTitleText.value.isNullOrEmpty()) {

            if (id.isEmpty()){
                callback.createNotesDialog(NotesModel(getNotesId(), notesTitleText.value!!, notesBodyText.value!!, Timestamp.now()))
            } else{
                callback.editNotesDialog(id, notesTitleText.value!!, notesBodyText.value!!)
            }
            dialog.dismiss()
        }
    }

    private fun getNotesId(): String {
        return firebaseRepository.getMainQuestId()
    }
}