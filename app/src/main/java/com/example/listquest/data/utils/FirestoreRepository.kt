package com.example.listquest.data.utils

import com.example.listquest.data.models.MainQuestModel
import com.example.listquest.data.models.NotesModel
import com.example.listquest.data.models.SideQuestModel
import com.example.listquest.data.models.UserModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class FirestoreRepository {
    val mainQuestRef = FirebaseFirestore.getInstance()


    fun addMainQuestToFirestore(mainQuest: MainQuestModel): Task<Void> {
        return mainQuestRef
            .collection("MainQuests")
            .document(mainQuest.id)
            .set(mainQuest)
    }

    fun addCompletedMainQuest(mainQuest:MainQuestModel): Task<Void>{
        return mainQuestRef
            .collection("CompletedQuests")
            .document(mainQuest.id)
            .set(mainQuest)
    }

    fun updateUser(userModel: UserModel){
        mainQuestRef
            .collection("Users")
            .document(userModel.id)
            .set(userModel)
    }

    fun updateUserDen(id: String, count: Int){
        mainQuestRef
            .collection("Users")
            .document(id)
            .update("levelUpDenominator", count)
    }

    fun updateUserLvl(id: String, count: Int){
        mainQuestRef
            .collection("Users")
            .document(id)
            .update("lvl", count)

    }


    fun getMainQuestId(): String {
        return mainQuestRef
            .collection("MainQuests")
            .document().id
    }

    fun getUserId(): String {
        return mainQuestRef
            .collection("User")
            .document().id
    }

    fun getNotesId(id: String): String{
        return mainQuestRef
            .collection("MainQuests")
            .document(id)
            .collection("notes")
            .document().id
    }

    fun getMainQuestQuery(): Query {
        return mainQuestRef
            .collection("MainQuests")
            .orderBy("timestamp", Query.Direction.DESCENDING)
    }

//    fun getCompletedQuestQuery(): Query {
//        return mainQuestRef
//            .collection("CompletedQuests")
//    }



    fun getSideQuestChkQuery(id: String): Query {
        return mainQuestRef
            .collection("MainQuests")
            .document(id)
            .collection("SideQuestCollection")
            .whereEqualTo("checked", true)
    }

    fun getSideQuestQuery(id: String): Query {
        return mainQuestRef
            .collection("MainQuests")
            .document(id)
            .collection("SideQuestCollection")
            .orderBy("timestamp", Query.Direction.ASCENDING)
    }

    fun getNotesQuery(id: String): Query {
        return mainQuestRef
            .collection("MainQuests")
            .document(id)
            .collection("notes")
            .orderBy("timestamp", Query.Direction.DESCENDING)
    }

    fun deleteNoteFromFirestore(id: String, noteId: String){
        mainQuestRef
            .collection("MainQuests")
            .document(id)
            .collection("notes")
            .document(noteId)
            .delete()
//            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
//            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

    fun deleteMainQuestToFirestore(mainQuest: MainQuestModel): Task<Void> {
        return mainQuestRef
            .collection("MainQuests")
            .document(mainQuest.id)
            .delete()
    }

    fun editMainQuest(mainQuest: MainQuestModel){
        mainQuestRef
            .collection("MainQuests")
            .document(mainQuest.id)
            .set(mainQuest)
    }

    fun completeMainQuest(mainQuest: MainQuestModel){
        mainQuestRef
            .collection("MainQuests")
            .document(mainQuest.id)
            .update("completed", true)
    }

    fun updateUservl(){

    }

    fun editSideQuestChk(mainQuest: MainQuestModel, sideQuest: SideQuestModel, isChecked: Boolean){
        mainQuestRef
            .collection("MainQuests")
            .document(mainQuest.id)
            .collection("SideQuestCollection")
            .document(sideQuest.id)
            .update("checked", isChecked)
    }

    fun addNotesToMainQuest(id: String, notes: NotesModel, noteId: String){
        mainQuestRef
            .collection("MainQuests")
            .document(id)
            .collection("notes")
            .document(noteId)
            .set(notes)
    }

    fun editNotes(id: String, noteId: String, title: String, body: String){
        getNotesDocument(id, noteId).update("title", title)
        getNotesDocument(id, noteId).update("body", body)

    }

    fun getNotesDocument(id: String, noteId: String): DocumentReference{
        return mainQuestRef
            .collection("MainQuests")
            .document(id)
            .collection("notes")
            .document(noteId)
    }

}