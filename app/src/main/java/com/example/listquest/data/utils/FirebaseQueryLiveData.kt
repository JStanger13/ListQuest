//package com.example.listquest.utils
//
//import android.util.Log
//import androidx.databinding.adapters.NumberPickerBindingAdapter.setValue
//import androidx.lifecycle.LiveData
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.Query
//
//
//
//class FirebaseQueryLiveData : LiveData<DataSnapshot> {
//    var ref = FirestoreRepository()
//    private val query: Query
//    private val listener = MyValueEventListener()
//
//    constructor(query: Query) {
//        this.query = query
//    }
//
//    constructor(ref: FirebaseFirestore) {
//        this.query = ref.
//    }
//
//    override fun onActive() {
//        Log.d(LOG_TAG, "onActive")
//        query.addValueEventListener(listener)
//    }
//
//    override fun onInactive() {
//        Log.d(LOG_TAG, "onInactive")
//        query.removeEventListener(listener)
//    }
//
//    private inner class MyValueEventListener : ValueEventListener {
//        fun onDataChange(dataSnapshot: DataSnapshot) {
//            setValue(dataSnapshot)
//        }
//
//        fun onCancelled(databaseError: DatabaseError) {
//            Log.e(LOG_TAG, "Can't listen to query $query", databaseError.toException())
//        }
//    }
//
//    companion object {
//        private val LOG_TAG = "FirebaseQueryLiveData"
//    }
//}