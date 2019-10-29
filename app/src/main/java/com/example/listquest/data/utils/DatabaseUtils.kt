package com.example.listquest.utils

import android.content.ContentValues
import android.util.Log
import com.example.listquest.data.models.MainQuestModel
import com.example.listquest.data.models.NotesModel
import com.example.listquest.data.models.SideQuestModel
import com.google.firebase.firestore.FirebaseFirestore

class DatabaseUtils {
    companion object {
        val mainQuestRef = FirebaseFirestore.getInstance().collection("MainQuests")

        var userId = ""

        var firebaseRepository = FirestoreRepository()

        fun addMainQuestToFirestore(mainQuest: MainQuestModel) {
            mainQuestRef.document(mainQuest.id)
                .set(mainQuest)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")
                }
                .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }
        }

        fun addSideQuestToFirestore(id: String, sideQuest: SideQuestModel) {
            mainQuestRef.document(id)
                .collection("SideQuestCollection")
                .document(sideQuest.id)
                .set(sideQuest)
        }

        fun addNotesToFirestore(id: String, note: NotesModel){
            mainQuestRef.document(id)
                .collection("notes")
                .document(note.id)
                .set(note)
        }



        fun deleteMainQuestFromFirestore(mainQuest: MainQuestModel) {
            mainQuestRef.document(mainQuest.id)
                .delete()
        }

//        fun editSideChk(mainQuest: MainQuestModel, sideQuest: SideQuestModel){
//            firebaseRepository.editSideQuestChk(mainQuest, sideQuest)
//        }

        fun deleteSideQuestFromFirestore(id: String, sideQuest: SideQuestModel) {
            mainQuestRef.document(id)
                .collection("SideQuestCollection")
                .document(sideQuest.id)
                .delete()
        }
    }
}