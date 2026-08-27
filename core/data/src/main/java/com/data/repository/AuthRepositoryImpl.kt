package com.data.repository

import com.data.model.AuthResult
import com.data.model.AuthUser
import com.data.model.PasswordResetResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.core.annotation.Single
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Single
class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
) : AuthRepository {
    override val currentUser: Flow<AuthUser?> =
        callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { firebaseAuth ->
                    trySend(firebaseAuth.currentUser?.toAuthUser())
                }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }

    override fun currentUserOrNull(): AuthUser? = auth.currentUser?.toAuthUser()

    override suspend fun signIn(email: String, password: String): AuthResult =
        runCatching {
            val result = auth.signInWithEmailAndPassword(email.trim(), password).await()
            AuthResult.Success(result.requireUser().toAuthUser())
        }.getOrElse { AuthResult.Error(it.toAuthMessage()) }

    override suspend fun signUp(name: String, email: String, password: String): AuthResult =
        runCatching {
            val result = auth.createUserWithEmailAndPassword(email.trim(), password).await()
            val user = result.requireUser()
            if (name.isNotBlank()) {
                val update = UserProfileChangeRequest.Builder().setDisplayName(name.trim()).build()
                user.updateProfile(update).await()
            }
            AuthResult.Success((auth.currentUser ?: user).toAuthUser())
        }.getOrElse { AuthResult.Error(it.toAuthMessage()) }

    override suspend fun signInWithGoogle(idToken: String): AuthResult =
        runCatching {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            AuthResult.Success(result.requireUser().toAuthUser())
        }.getOrElse { AuthResult.Error(it.toAuthMessage()) }

    // fetchSignInMethodsForEmail is deprecated because e-mail enumeration protection makes it
    // return an empty list; when the project has protection enabled this check simply falls
    // through to sending the e-mail, which is the correct (privacy-preserving) behavior anyway.
    @Suppress("DEPRECATION")
    override suspend fun sendPasswordReset(email: String): PasswordResetResult =
        runCatching {
            val methods =
                auth.fetchSignInMethodsForEmail(email.trim()).await().signInMethods.orEmpty()
            val hasPassword = EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD in methods
            val hasGoogle = GoogleAuthProvider.GOOGLE_SIGN_IN_METHOD in methods
            if (hasGoogle && !hasPassword) {
                PasswordResetResult.GoogleOnly
            } else {
                auth.sendPasswordResetEmail(email.trim()).await()
                PasswordResetResult.Sent
            }
        }.getOrElse { PasswordResetResult.Error(it.toAuthMessage()) }

    override suspend fun getIdToken(forceRefresh: Boolean): String? =
        auth.currentUser?.getIdToken(forceRefresh)?.await()?.token

    override fun signOut() = auth.signOut()

    private fun com.google.firebase.auth.AuthResult.requireUser(): FirebaseUser =
        user ?: error("Firebase retornou um usuário nulo.")

    private fun FirebaseUser.toAuthUser(): AuthUser =
        AuthUser(
            uid = uid,
            name = displayName,
            email = email,
            photoUrl = photoUrl?.toString(),
        )

    private fun Throwable.toAuthMessage(): String =
        when (this) {
            is FirebaseAuthWeakPasswordException -> "A senha deve ter ao menos 6 caracteres."
            is FirebaseAuthInvalidCredentialsException -> "E-mail ou senha inválidos."
            is FirebaseAuthInvalidUserException -> "Conta não encontrada."
            is FirebaseAuthUserCollisionException -> "Já existe uma conta com este e-mail."
            else -> message ?: "Falha na autenticação. Tente novamente."
        }
}

/** Bridges a Play Services [Task] into a suspending call, without pulling in coroutines-play-services. */
private suspend fun <T> Task<T>.await(): T =
    suspendCancellableCoroutine { continuation ->
        addOnSuccessListener { continuation.resume(it) }
        addOnFailureListener { continuation.resumeWithException(it) }
    }
