package com.stanger.listquest.utils


import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.stanger.listquest.data.models.MainQuestModel
import com.stanger.listquest.data.models.SideQuestModel
import com.stanger.listquest.data.models.UserModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class FirestoreRepository {

    private val currentUserDocRef: DocumentReference
        get() = mainQuestRef.document("Users/${ FirebaseAuth.getInstance().uid
            ?: throw NullPointerException("UID is null.") as Throwable}")

    val storageRef = FirebaseStorage.getInstance().reference

    private val mainQuestRef = FirebaseFirestore.getInstance()

    fun storeImage(uri: Uri): StorageReference{
        return storageRef.child("photos").child(uri.lastPathSegment!!)
    }

    private val settings = FirebaseFirestoreSettings.Builder()
        .setPersistenceEnabled(true)
        .build()

    init {
        mainQuestRef.firestoreSettings = settings
    }

    fun getUser(): DocumentReference{
        return currentUserDocRef
    }

    fun addMainQuestToFirestore(mainQuest: MainQuestModel, userModel: UserModel): Task<Void> {
        return mainQuestRef
            .collection("MainQuests" + userModel.id)
            .document(mainQuest.id)
            .set(mainQuest)
    }

    fun addCompletedMainQuest(mainQuest:MainQuestModel, userModel: UserModel): Task<Void>{
        return mainQuestRef
            .collection("CompletedQuests" + userModel.id)
            .document(mainQuest.id)
            .set(mainQuest)
    }

    fun addFavoriteQuestToFirestore(mainQuest:MainQuestModel, userModel: UserModel): Task<Void>{
        return mainQuestRef
            .collection("FavoritesQuests" + userModel.id)
            .document(mainQuest.id)
            .set(mainQuest)
    }

    fun deleteFavoriteQuestToFirestore(mainQuest:MainQuestModel, userModel: UserModel) {
        deleteFavoriteSnap(mainQuest.id, userModel)
        mainQuestRef
            .collection("FavoritesQuests")
            .document(mainQuest.id)
            .get()
            .addOnSuccessListener {
                if ( it != null ) it.reference.delete()
                else Log . d ( TAG , "No such document" )
            }
    }

    fun deleteCompletedQuest(mainQuest: MainQuestModel, userModel: UserModel){
        deleteCompletedSnap(mainQuest.id, userModel)
        mainQuestRef
            .collection("CompletedQuest")
            .document(mainQuest.id)
            .get()
            .addOnSuccessListener {
                if ( it != null ) it.reference.delete()
                else Log . d ( TAG , "No such document")
            }
    }

    fun addSideQuestToFirestore(mainQuest: MainQuestModel, sideQuest: SideQuestModel,userModel: UserModel){
        mainQuestRef
            .collection("MainQuests" + userModel.id)
            .document(mainQuest.id)
            .collection("SideQuestCollection")
            .document(sideQuest.id)
            .set(sideQuest)
    }

    fun addSideQuestToFavorites(mainQuest: MainQuestModel, sideQuest: SideQuestModel,userModel: UserModel){
        mainQuestRef
            .collection("FavoritesQuests" + userModel.id)
            .document(mainQuest.id)
            .collection("SideQuestCollection")
            .document(sideQuest.id)
            .set(sideQuest)
    }

    fun updateUser(userModel: UserModel){
        mainQuestRef
            .collection("Users")
            .document(userModel.id)
            .set(userModel)
    }

    fun getMainQuestId(): String {
        return mainQuestRef
            .collection("MainQuests")
            .document().id
    }

    fun getMainQuestQuery(userModel: UserModel): Query {
        return mainQuestRef
            .collection("MainQuests" + userModel.id)
            .orderBy("timestamp", Query.Direction.DESCENDING)
    }

    fun getPhotoQuery(userModel: UserModel): Query {
        return mainQuestRef
            .collection("images" + userModel.id)
            .orderBy("timestamp", Query.Direction.ASCENDING)
    }

    fun getCompletedQuestsQuery(userModel: UserModel): Query {
        return mainQuestRef
            .collection("CompletedQuests" + userModel.id)
            .orderBy("timestamp", Query.Direction.DESCENDING)
    }

    fun getFavoritesQuestsQuery(userModel: UserModel): Query {
        return mainQuestRef
            .collection("FavoritesQuests" + userModel.id)
            .orderBy("timestamp", Query.Direction.DESCENDING)
    }

    fun getFavoritesSideQuestsQuery(id: String, userModel: UserModel): Query {
        return mainQuestRef
            .collection("FavoritesQuests" + userModel.id)
            .document(id)
            .collection("SideQuestCollection")
            .orderBy("timestamp", Query.Direction.ASCENDING)    }

    fun getCompletedSideQuestsQuery(id: String, userModel: UserModel): Query {
        return mainQuestRef
            .collection("CompletedQuests" + userModel.id)
            .document(id)
            .collection("SideQuestCollection")
            .orderBy("timestamp", Query.Direction.ASCENDING)
    }

    fun getSideQuestChkQuery(id: String, userModel: UserModel): Query {
        return mainQuestRef
            .collection("MainQuests" + userModel.id)
            .document(id)
            .collection("SideQuestCollection")
            .whereEqualTo("checked", true)
    }

    fun getSideQuestQuery(id: String, userModel: UserModel): Query {
        return mainQuestRef
            .collection("MainQuests" + userModel.id)
            .document(id)
            .collection("SideQuestCollection")
            .orderBy("timestamp", Query.Direction.ASCENDING)
    }

    fun deleteMainQuestToFirestore(mainQuest: MainQuestModel, userModel: UserModel) {
        deleteSnap(mainQuest.id, userModel)
        mainQuestRef
            .collection("MainQuests" + userModel.id)
            .document(mainQuest.id)
            .get()
            .addOnSuccessListener {
                if (it != null) {
                    it.reference.delete()
                    Log.d ( TAG , "DeleteSuccessful")
                }
                else Log.d ( TAG , "No such document" )
            }
    }

    private fun deleteCompletedSnap(id: String, userModel: UserModel) {
        getCompletedSideQuestsQuery(id, userModel)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                } else {
                    for(document in snapshots!!.documents){
                        document.reference.delete()
                    }
                }
            }
    }

    private fun deleteFavoriteSnap(id: String, userModel: UserModel) {
        getFavoritesSideQuestsQuery(id, userModel)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                } else {
                    for(document in snapshots!!.documents){
                        document.reference.delete()
                    }
                }
            }
    }

    private fun deleteSnap(id: String, userModel: UserModel) {
        getSideQuestQuery(id, userModel)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                } else {
                    for(document in snapshots!!.documents){
                        document.reference.delete()
                    }
                }
            }
    }

    fun deleteSnapMain(userModel: UserModel){
        getMainQuestQuery(userModel)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                } else {
                    for (document in snapshots!!.documents) {
                        val main = document.reference
                        deleteSnap(main.id, userModel)
                        main.delete()
                    }
                }
            }
    }
    fun deleteSnapMainFavorite(userModel: UserModel){
        getFavoritesQuestsQuery(userModel)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                } else {
                    for (document in snapshots!!.documents) {
                        val main = document.reference
                        deleteSnap(main.id, userModel)
                        main.delete()
                    }
                }
            }
    }

    fun deleteSnapMainCompleted(userModel: UserModel){
        getCompletedQuestsQuery(userModel)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                } else {
                    for (document in snapshots!!.documents) {
                        val main = document.reference
                        deleteSnap(main.id, userModel)
                        main.delete()
                    }
                }
            }
    }

    fun deleteSnapImagesCompleted(userModel: UserModel){
        getPhotoQuery(userModel)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                } else {
                    for (document in snapshots!!.documents) {
                        val main = document.reference
                        deleteSnap(main.id, userModel)
                        main.delete()
                    }
                }
            }
    }


    fun editMainQuest(mainQuest: MainQuestModel, userModel: UserModel){
        mainQuestRef
            .collection("MainQuests" + userModel.id)
            .document(mainQuest.id)
            .set(mainQuest)
    }

    fun editFavoritesQuest(mainQuest: MainQuestModel, userModel: UserModel){
        mainQuestRef
            .collection("FavoritesQuests" + userModel.id)
            .document(mainQuest.id)
            .set(mainQuest)
    }

    fun editMainQuestCompleted(mainQuest: MainQuestModel, boolean: Boolean, userModel: UserModel){
        mainQuestRef
            .collection("MainQuests" + userModel.id)
            .document(mainQuest.id)
            .update("completed", boolean)
    }


    fun editSideQuest(mainQuest: MainQuestModel, sideQuest: SideQuestModel, userModel: UserModel){
        mainQuestRef
            .collection("MainQuests" + userModel.id)
            .document(mainQuest.id)
            .collection("SideQuestCollection")
            .document(sideQuest.id)
            .update("sideQuestTitle", sideQuest.sideQuestTitle)
    }

    fun editSideQuestChk(mainQuest: MainQuestModel, sideQuest: SideQuestModel, isChecked: Boolean, userModel: UserModel){
        mainQuestRef
            .collection("MainQuests" + userModel.id)
            .document(mainQuest.id)
            .collection("SideQuestCollection")
            .document(sideQuest.id)
            .update("checked", isChecked)
    }

    fun editFavoriteChk(mainQuest: MainQuestModel, sideQuest: SideQuestModel, isChecked: Boolean, userModel: UserModel){
        mainQuestRef
            .collection("FavoritesQuests" + userModel.id)
            .document(mainQuest.id)
            .collection("SideQuestCollection")
            .document(sideQuest.id)
            .update("checked", isChecked)
    }
}