package com.stanger.listquest.utils


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.stanger.listquest.data.models.UserModel

object FirestoreUtil{

    lateinit var userModel: UserModel
    lateinit var collection: CollectionReference
    var isLevelUp = false

    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance()}

    private val currentUserDocRef: DocumentReference
        get() = firestoreInstance.document("Users/${ FirebaseAuth.getInstance().uid
            ?: throw NullPointerException("UID is null.")}")
}