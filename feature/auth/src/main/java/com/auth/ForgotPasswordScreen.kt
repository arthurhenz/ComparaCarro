package com.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.theme.Theme
import com.theme.TokenSpacing
import com.ui.PrimaryButton

@Composable
fun ForgotPasswordScreen(
    isLoading: Boolean = false,
    errorMessage: String? = null,
    emailSent: Boolean = false,
    googleOnlyAccount: Boolean = false,
    onSubmit: (email: String) -> Unit = {},
    onBackToLogin: () -> Unit = {},
) {
    var email by rememberSaveable { mutableStateOf("") }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Theme.colors.background)
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .imePadding()
                .verticalScroll(rememberScrollState()),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onBackToLogin) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Theme.colors.textPrimary,
                )
            }
        }

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = TokenSpacing.Section)
                    .padding(bottom = TokenSpacing.Section),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ForgotPasswordHero()

            Spacer(modifier = Modifier.height(TokenSpacing.Section))

            if (googleOnlyAccount) {
                GoogleAccountNotice(onBackToLogin = onBackToLogin)
            } else if (emailSent) {
                SentConfirmation(onBackToLogin = onBackToLogin)
            } else {
                Text(
                    text = "Informe seu e-mail e enviaremos um link para redefinir sua senha.",
                    style = Theme.typography.bodyMedium,
                    color = Theme.colors.textSecondary,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(TokenSpacing.Section))

                AccountTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "E-mail",
                    placeholder = "voce@email.com",
                    icon = Icons.Filled.MailOutline,
                    keyboardType = KeyboardType.Email,
                )

                AuthErrorText(errorMessage)

                Spacer(modifier = Modifier.height(TokenSpacing.Section))

                PrimaryButton(
                    text = if (isLoading) "ENVIANDO..." else "ENVIAR LINK",
                    enabled = !isLoading,
                    onClick = { onSubmit(email) },
                )

                Spacer(modifier = Modifier.height(TokenSpacing.Section))

                SwitchAuthLink(
                    prompt = "Lembrou a senha?",
                    action = "Entrar",
                    onClick = onBackToLogin,
                )
            }
        }
    }
}

@Composable
private fun ForgotPasswordHero() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier =
                Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(brush = Theme.colors.interactivePrimary),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.LockReset,
                contentDescription = null,
                tint = Theme.colors.textInteractive,
                modifier = Modifier.size(48.dp),
            )
        }
        Spacer(modifier = Modifier.height(TokenSpacing.Block))
        Text(
            text = "Recuperar senha",
            style = Theme.typography.headlineLarge,
            color = Theme.colors.accentPrimary,
        )
    }
}

@Composable
private fun SentConfirmation(onBackToLogin: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = Theme.colors.accentPrimary,
            modifier = Modifier.size(48.dp),
        )

        Spacer(modifier = Modifier.height(TokenSpacing.Block))

        // Firebase does not reveal whether the address has an account (enumeration protection),
        // so the confirmation is deliberately phrased as conditional and already covers the
        // Google-only case that the client cannot detect.
        Text(
            text =
                "Pedido enviado! Se existir uma conta com este e-mail, " +
                    "você receberá um link para redefinir a senha.",
            style = Theme.typography.bodyMedium,
            color = Theme.colors.textSecondary,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(TokenSpacing.Block))

        Text(
            text =
                "Criou sua conta com o Google? Ela não possui senha — " +
                    "volte ao login e toque em \"Continuar com Google\".",
            style = Theme.typography.bodyMedium,
            color = Theme.colors.textSecondary,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(TokenSpacing.Section))

        PrimaryButton(
            text = "VOLTAR PARA O LOGIN",
            onClick = onBackToLogin,
        )
    }
}

@Composable
private fun GoogleAccountNotice(onBackToLogin: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier =
                Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Theme.colors.surfaceRaised, shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "G",
                style = Theme.typography.headlineLarge,
                color = Theme.colors.accentPrimary,
            )
        }

        Spacer(modifier = Modifier.height(TokenSpacing.Block))

        Text(
            text =
                "Esta conta foi criada com o Google e não possui senha. " +
                    "Volte ao login e toque em \"Continuar com Google\".",
            style = Theme.typography.bodyMedium,
            color = Theme.colors.textSecondary,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(TokenSpacing.Section))

        PrimaryButton(
            text = "VOLTAR PARA O LOGIN",
            onClick = onBackToLogin,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    Theme {
        ForgotPasswordScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenSentPreview() {
    Theme {
        ForgotPasswordScreen(emailSent = true)
    }
}
