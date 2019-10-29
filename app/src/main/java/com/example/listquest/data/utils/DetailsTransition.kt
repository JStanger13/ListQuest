package com.example.listquest.utils

import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.ChangeTransform
import androidx.transition.TransitionSet

class DetailsTransition : TransitionSet() {
    fun DetailsTransition() {
        setOrdering(ORDERING_TOGETHER);
        addTransition( ChangeBounds()).
            addTransition( ChangeTransform()).
            addTransition( ChangeImageTransform())
    }
}