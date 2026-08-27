package com.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

/**
 * Retrieves a Google ID token through Credential Manager on the UI layer; the token is then handed
 * to [com.data.repository.AuthRepository.signInWithGoogle] which exchanges it for a Firebase session.
 *
 * SETUP REQUIRED before the Google button works:
 *  1. In the Firebase console enable Authentication → Sign-in method → Google.
 *  2. Add your app's SHA-1 (and SHA-256) fingerprint under Project settings → Your apps.
 *  3. Re-download `google-services.json` (its `oauth_client` array must now be populated).
 *  4. Copy the *Web* client id (`client_type: 3`) into [WEB_CLIENT_ID] below.
 * Until then [isConfigured] is false and the UI reports that Google Sign-In is unavailable.
 */
object GoogleSignInHelper {
    private const val PLACEHOLDER = "TODO_REPLACE_WITH_WEB_CLIENT_ID"

    // TODO: replace with the Web client id from google-services.json (oauth_client, client_type 3).
    const val WEB_CLIENT_ID: String = "413351987684-gabbannl3h6kek0093olvpda9r29kcf3.apps.googleusercontent.com"

    val isConfigured: Boolean get() = WEB_CLIENT_ID != PLACEHOLDER

    /** Launches the Google account picker and returns the selected account's ID token. */
    suspend fun getIdToken(context: Context): String {
        val option =
            GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(WEB_CLIENT_ID)
                .build()
        val request = GetCredentialRequest.Builder().addCredentialOption(option).build()
        val response = CredentialManager.create(context).getCredential(context, request)
        val credential = response.credential
        if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            return GoogleIdTokenCredential.createFrom(credential.data).idToken
        }
        error("Credencial do Google inválida.")
    }
}
