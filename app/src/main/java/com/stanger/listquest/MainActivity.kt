package com.stanger.listquest

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.stanger.listquest.data.models.UserModel
import com.stanger.listquest.data.utils.DialogUtils.Companion.imm
import com.stanger.listquest.data.utils.FirestoreRepository
import com.stanger.listquest.ui.login.LoginFragment
import com.stanger.listquest.ui.mainquest.MainQuestFragment
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity(){

    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance()}
    private val currentUserDocRef: DocumentReference
        get() = firestoreInstance.document("Users/${ FirebaseAuth.getInstance().uid
            ?: throw NullPointerException("UID is null.")}")

    companion object {
        var main = MainActivity
        val loginFragment = LoginFragment.newInstance()
        var firebaseRepository = FirestoreRepository()
    }

    override fun dispatchGenericMotionEvent(ev: MotionEvent?): Boolean {
        if (ev!!.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    v.clearFocus();
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        container.visibility = View.VISIBLE

        val firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser

        if (savedInstanceState == null) {
            if (firebaseUser == null) {
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left)
                    .add(R.id.container, loginFragment, "")
                    .commitNow()
            } else {
                firebaseRepository.getUser().get().addOnSuccessListener {
                    if (it.exists()) {
                        val userModel = it.toObject(UserModel::class.java)!!
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left)
                            .add(R.id.container, MainQuestFragment(userModel), "")
                            .commitNow()
                    } else {
                        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
                            if (!documentSnapshot.exists()) {
                                val user = FirebaseAuth.getInstance().currentUser
                                val newUser = UserModel(
                                    user!!.uid,
                                    user.displayName ?: "",
                                    user.photoUrl.toString(),
                                    1,
                                    1,
                                    0,
                                    0,
                                    0
                                )
                                supportFragmentManager.beginTransaction()
                                    .setCustomAnimations(
                                        R.anim.enter_from_right,
                                        R.anim.exit_from_left
                                    )
                                    .add(R.id.container, MainQuestFragment(newUser), "")
                                    .commitNow()
                            }
                        }
                    }
                }
            }
        }
    }
}

fun View.hideKeyboard() {
    val inputMethodManager = context!!.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    inputMethodManager?.hideSoftInputFromWindow(this.windowToken, 0)
}

fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}