package com.stanger.listquest.utils


import android.content.ContentValues
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.stanger.listquest.data.models.MainQuestModel
import com.stanger.listquest.data.models.SideQuestModel
import com.stanger.listquest.data.models.UserModel


class DatabaseUtils {
    companion object {
        val mainQuestRef = FirebaseFirestore.getInstance().collection("MainQuests")
        val favoriteQuestRef = FirebaseFirestore.getInstance().collection("FavoritesQuests")
        val imageRef = FirebaseFirestore.getInstance().collection("images")

        fun handleLvlUp(num: Int, dem: Int): Float{
            if(num == 0){
                return 0f
            }

            return num.toFloat()/dem.toFloat()
        }


        var userId = ""

        //lateinit var userModel: UserModel

        fun getMainQuestId(): String {
            return mainQuestRef
                .document().id
        }


        var firebaseRepository = FirestoreRepository()

        //        fun addPictureToFirestore(id: String, path: String, bool: Boolean){
//            imageRef
//                .document(id)
//                .set(1PhotoModel(id, path, bool, Timestamp.now()))
//        }
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

        fun addSideQuestFavoriteToFirestore(id: String, sideQuest: SideQuestModel, userModel: UserModel) {
            favoriteQuestRef.document(id)
                .collection("SideQuestCollection"  + userModel.id)
                .document(sideQuest.id)
                .set(sideQuest)
        }


//        fun addNotesToFirestore(id: String, note: NotesModel){
//            mainQuestRef.document(id)
//                .collection("notes")
//                .document(note.id)
//                .set(note)
//        }



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