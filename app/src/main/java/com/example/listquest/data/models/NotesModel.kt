package com.example.listquest.data.models

import com.google.firebase.Timestamp
import java.io.Serializable
import java.util.*

data class NotesModel(
    var id: String =  "",
    var title: String = "",
    var notesBody: String  = "",
    var timestamp: Timestamp

): Serializable, Observable() {
    constructor() : this("", "", "", Timestamp.now())
}