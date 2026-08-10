package com.development.legally.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@IgnoreExtraProperties
data class Case(
    // This property gets the unique Document ID (the auto-generated string)
    // We name it something that won't collide with any field in your document
    @DocumentId
    val firestoreDocId: String = "",

    // This maps to the actual "id" field stored inside the document data
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id: String = "",

    @get:PropertyName("caseNumber")
    @set:PropertyName("caseNumber")
    var caseNumber: String = "",

    @get:PropertyName("CaseTittle")
    @set:PropertyName("CaseTittle")
    var CaseTittle: String = "",

    @get:PropertyName("clientId")
    @set:PropertyName("clientId")
    var clientId: String = "",

    @get:PropertyName("clientName")
    @set:PropertyName("clientName")
    var clientName: String = "",

    @get:PropertyName("processType")
    @set:PropertyName("processType")
    var processType: String = "",

    @get:PropertyName("status")
    @set:PropertyName("status")
    var status: String = "",

    @get:PropertyName("priority")
    @set:PropertyName("priority")
    var priority: String = "",

    @get:PropertyName("description")
    @set:PropertyName("description")
    var description: String = "",

    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
)
