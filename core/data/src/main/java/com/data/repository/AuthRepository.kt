package com.data.repository

import com.data.model.AuthResult
import com.data.model.AuthUser
import com.data.model.PasswordResetResult
import kotlinx.coroutines.flow.Flow

/**
 * Firebase Auth wrapper. Firebase owns the session: it persists the signed-in user across launches
 * and refreshes the ID token automatically, so callers only observe [currentUser] and, when they
 * need to authenticate a backend call, read [getIdToken]. No token is stored by the app itself.
 */
interface AuthRepository {
    /** Emits the current user on every auth state change; `null` while signed out. */
    val currentUser: Flow<AuthUser?>

    /** The signed-in user right now, or `null`. Synchronous convenience for one-off reads. */
    fun currentUserOrNull(): AuthUser?

    suspend fun signIn(email: String, password: String): AuthResult

    suspend fun signUp(name: String, email: String, password: String): AuthResult

    /** Signs in with a Google ID token obtained from Credential Manager on the UI layer. */
    suspend fun signInWithGoogle(idToken: String): AuthResult

    /**
     * Sends the Firebase password-reset e-mail. Returns [PasswordResetResult.GoogleOnly] when the
     * account has no password provider (Google-only), so the UI can point the user at Google
     * sign-in instead. With e-mail enumeration protection enabled, Firebase hides both the
     * provider list and whether the address exists, so [PasswordResetResult.Sent] only means
     * "request accepted".
     */
    suspend fun sendPasswordReset(email: String): PasswordResetResult

    /**
     * The Firebase ID token for the current user, refreshed if [forceRefresh] is true or the cached
     * token is stale. `null` when signed out. Attach as a `Bearer` header to authenticate a backend.
     */
    suspend fun getIdToken(forceRefresh: Boolean = false): String?

    fun signOut()
}
