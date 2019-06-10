package com.example.listquest.utils

import android.content.ContentValues
import android.util.Log
import com.example.listquest.models.MainQuestModel
import com.example.listquest.models.SideQuestModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class FirestoreRepository {
    companion object {
        val mainQuestRef = FirebaseFirestore.getInstance().collection("MainQuests")
        val query = mainQuestRef.orderBy("timeStamp", Query.Direction.ASCENDING)

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

        fun deleteMainQuestFromFirestore(mainQuest: MainQuestModel) {
            mainQuestRef.document(mainQuest.id)
                .delete()
        }

        fun deleteSideQuestFromFirestore(id: String, sideQuest: SideQuestModel) {
            mainQuestRef.document(id)
                .collection("SideQuestCollection")
                .document(sideQuest.id)
                .delete()
        }
    }
}