package com.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.navigation.EntryProvider
import com.navigation.routes.ForgotPasswordRoute
import com.navigation.routes.LoginRoute
import com.navigation.routes.SignupRoute
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

/**
 * The login screen wired to its [LoginViewModel] and the Google credential flow. Exposed as a
 * standalone composable so it can be rendered both as the [LoginRoute] entry and inline wherever a
 * signed-out user must authenticate (e.g. the Profile tab).
 */
@Composable
fun LoginScreenHost() {
    val viewModel: LoginViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LoginScreen(
        isLoading = state.isLoading,
        errorMessage = state.errorMessage,
        onSubmit = viewModel::login,
        onGoogleLogin = {
            if (!GoogleSignInHelper.isConfigured) {
                viewModel.onGoogleFailed("Login com Google ainda não configurado.")
            } else {
                scope.launch {
                    viewModel.onGoogleStarted()
                    try {
                        viewModel.loginWithGoogle(GoogleSignInHelper.getIdToken(context))
                    } catch (e: Exception) {
                        viewModel.onGoogleFailed(e.message ?: "Falha no login com Google.")
                    }
                }
            }
        },
        onForgotPassword = viewModel::goToForgotPassword,
        onCreateAccount = viewModel::goToSignup,
        onContinueWithoutLogin = viewModel::continueWithoutLogin,
    )
}

fun EntryProviderScope<NavKey>.loginScreenRoute() {
    entry<LoginRoute> { LoginScreenHost() }
}

fun EntryProviderScope<NavKey>.signupScreenRoute() {
    entry<SignupRoute> {
        val viewModel: SignupViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val context = LocalContext.current
        val scope = rememberCoroutineScope()

        SignupScreen(
            isLoading = state.isLoading,
            errorMessage = state.errorMessage,
            onSubmit = viewModel::signup,
            onGoogleSignup = {
                if (!GoogleSignInHelper.isConfigured) {
                    viewModel.onGoogleFailed("Cadastro com Google ainda não configurado.")
                } else {
                    scope.launch {
                        viewModel.onGoogleStarted()
                        try {
                            viewModel.signupWithGoogle(GoogleSignInHelper.getIdToken(context))
                        } catch (e: Exception) {
                            viewModel.onGoogleFailed(e.message ?: "Falha no cadastro com Google.")
                        }
                    }
                }
            },
            onLoginInstead = viewModel::goToLogin,
        )
    }
}

fun EntryProviderScope<NavKey>.forgotPasswordScreenRoute() {
    entry<ForgotPasswordRoute> {
        val viewModel: ForgotPasswordViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        ForgotPasswordScreen(
            isLoading = state.isLoading,
            errorMessage = state.errorMessage,
            emailSent = state.emailSent,
            googleOnlyAccount = state.googleOnlyAccount,
            onSubmit = viewModel::sendResetLink,
            onBackToLogin = viewModel::backToLogin,
        )
    }
}

class AuthScreenProvider : EntryProvider {
    override fun entryProvider(): EntryProviderScope<NavKey>.() -> Unit = {
        loginScreenRoute()
        signupScreenRoute()
        forgotPasswordScreenRoute()
    }
}
