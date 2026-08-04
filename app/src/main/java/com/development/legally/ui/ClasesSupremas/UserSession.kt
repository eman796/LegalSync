package com.development.legally.ui.ClasesSupremas

object UserSession {
    var currentUser: com.development.legally.data.model.User? = null

    val isLawyer: Boolean
        get() = currentUser?.role == "lawyer"

    val isSecretary: Boolean
        get() = currentUser?.role == "secretary"

    val isApproved: Boolean
        get() = currentUser?.isApproved == true

    fun clear() {
        currentUser = null
    }
}