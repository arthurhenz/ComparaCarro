package com.data.model

/**
 * The signed-in user, mapped from Firebase's `FirebaseUser` so the rest of the app never depends on
 * the Firebase SDK. `null` anywhere this type is expected means "signed out".
 */
data class AuthUser(
    val uid: String,
    val name: String?,
    val email: String?,
    val photoUrl: String?,
)
