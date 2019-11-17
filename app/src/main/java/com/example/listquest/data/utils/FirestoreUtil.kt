package com.example.listquest.data.utils

import com.example.listquest.data.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


object FirestoreUtil{

    lateinit var userModel: UserModel
    var isLevelUp = false

    private val firestoreInstance: FirebaseFirestore by lazy {FirebaseFirestore.getInstance()}

    private val currentUserDocRef: DocumentReference
        get() = firestoreInstance.document("Users/${ FirebaseAuth.getInstance().uid
        ?: throw NullPointerException("UID is null.")}")

    fun initCurrentUserIfFirstTime(onComplete: () -> Unit){
        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
            if(!documentSnapshot.exists()){
                var user = FirebaseAuth.getInstance().currentUser
                val newUser = UserModel(
                    user!!.uid,
                    user!!.displayName?: "",
                    user.photoUrl.toString(),
                    1,
                    1,
                    0,
                    0,
                    0
                    )
                userModel = newUser
                currentUserDocRef.set(newUser).addOnSuccessListener {
                    onComplete()
                }
            }

            else {
                userModel = documentSnapshot.toObject(UserModel::class.java)!!
                onComplete()
            }
        }
    }

    fun updateCurrentUser(name: String = "",
                          profilePicturePath: String? = null,
                          lvl: Int = 1,
                          den: Int = 1,
                          availQuests: Int = 0,
                          completed: Int = 0,
                          numerator: Int = 0 ){
        val userFieldMap = mutableMapOf<String, Any>()

        if(lvl>1) userFieldMap["lvl"] = lvl
        if(den>1) userFieldMap["den"] = den
        if(availQuests>0) userFieldMap["availQuests"] = availQuests
        if(completed>0) userFieldMap["completed"] = completed
        if(numerator>0) userFieldMap["numerator"] = numerator

        if (name.isNotBlank()) userFieldMap["name"] = name
        if (profilePicturePath != null) userFieldMap["profilePicturePath"] = profilePicturePath
        currentUserDocRef.update(userFieldMap)
    }

    fun getCurrentUser(onComplete: (UserModel) -> Unit) {
        currentUserDocRef.get()
            .addOnSuccessListener {
                onComplete(it.toObject(UserModel::class.java)!!)
            }
    }
}