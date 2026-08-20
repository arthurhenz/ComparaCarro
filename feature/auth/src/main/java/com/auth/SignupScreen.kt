package com.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.theme.Theme
import com.theme.TokenIconSize
import com.theme.TokenSpacing
import com.ui.PrimaryButton

private const val MIN_PASSWORD_LENGTH = 8
private val NAME_REGEX = Regex("^[A-Za-zÀ-ÖØ-öø-ÿ]+(?:\\s[A-Za-zÀ-ÖØ-öø-ÿ]+)+$")
private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

@Composable
fun SignupScreen(
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onSubmit: (name: String, email: String, password: String) -> Unit = { _, _, _ -> },
    onLoginInstead: () -> Unit = {},
) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirm by rememberSaveable { mutableStateOf("") }
    var acceptTerms by rememberSaveable { mutableStateOf(false) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmVisible by rememberSaveable { mutableStateOf(false) }

    var nameTouched by rememberSaveable { mutableStateOf(false) }
    var emailTouched by rememberSaveable { mutableStateOf(false) }
    var passwordTouched by rememberSaveable { mutableStateOf(false) }
    var confirmTouched by rememberSaveable { mutableStateOf(false) }

    val nameValid = NAME_REGEX.matches(name.trim())
    val emailValid = EMAIL_REGEX.matches(email.trim())
    val passwordLongEnough = password.length >= MIN_PASSWORD_LENGTH
    val passwordsMatch = password == confirm
    val canSubmit =
        acceptTerms && nameValid && emailValid && passwordLongEnough && passwordsMatch && !isLoading

    val nameError = if (nameTouched && !nameValid) "Informe nome e sobrenome." else null
    val emailError = if (emailTouched && !emailValid) "Informe um e-mail válido." else null
    val passwordError =
        if (passwordTouched && !passwordLongEnough) {
            "A senha deve ter ao menos $MIN_PASSWORD_LENGTH caracteres."
        } else {
            null
        }
    val confirmError = if (confirmTouched && !passwordsMatch) "As senhas não coincidem." else null

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Theme.colors.background)
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = TokenSpacing.Section, vertical = TokenSpacing.Section),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Voltar",
                tint = Theme.colors.textPrimary,
                modifier =
                    Modifier
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                        ) { onLoginInstead() }
                        .padding(TokenSpacing.Inline),
            )
        }

        SignupHero()

        Spacer(modifier = Modifier.height(TokenSpacing.Section))

        AccountTextField(
            value = name,
            onValueChange = { name = it },
            label = "Nome Completo",
            placeholder = "Seu nome",
            icon = Icons.Filled.Person,
            errorText = nameError,
            onFocusLost = { nameTouched = true },
        )

        Spacer(modifier = Modifier.height(TokenSpacing.Block))

        AccountTextField(
            value = email,
            onValueChange = { email = it },
            label = "E-mail",
            placeholder = "voce@email.com",
            icon = Icons.Filled.MailOutline,
            keyboardType = KeyboardType.Email,
            errorText = emailError,
            onFocusLost = { emailTouched = true },
        )

        Spacer(modifier = Modifier.height(TokenSpacing.Block))

        AccountTextField(
            value = password,
            onValueChange = { password = it },
            label = "Senha",
            placeholder = "Mínimo $MIN_PASSWORD_LENGTH caracteres",
            icon = Icons.Filled.Lock,
            keyboardType = KeyboardType.Password,
            visualTransformation =
                if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                PasswordVisibilityToggle(
                    visible = passwordVisible,
                    onToggle = { passwordVisible = !passwordVisible },
                )
            },
            errorText = passwordError,
            onFocusLost = { passwordTouched = true },
        )

        Spacer(modifier = Modifier.height(TokenSpacing.Block))

        AccountTextField(
            value = confirm,
            onValueChange = { confirm = it },
            label = "Confirmar Senha",
            placeholder = "Repita a senha",
            icon = Icons.Filled.Shield,
            keyboardType = KeyboardType.Password,
            visualTransformation =
                if (confirmVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                PasswordVisibilityToggle(
                    visible = confirmVisible,
                    onToggle = { confirmVisible = !confirmVisible },
                )
            },
            errorText = confirmError,
            onFocusLost = { confirmTouched = true },
        )

        Spacer(modifier = Modifier.height(TokenSpacing.Block))

        TermsRow(
            checked = acceptTerms,
            onCheckedChange = { acceptTerms = it },
        )

        AuthErrorText(errorMessage)

        Spacer(modifier = Modifier.height(TokenSpacing.Section))

        PrimaryButton(
            text = if (isLoading) "CRIANDO..." else "CRIAR CONTA",
            enabled = canSubmit,
            onClick = { onSubmit(name, email, password) },
        )

        if (!acceptTerms) {
            Spacer(modifier = Modifier.height(TokenSpacing.Item))
            Text(
                text = "Aceite os termos para continuar.",
                style = Theme.typography.labelMedium,
                color = Theme.colors.textSecondary,
                textAlign = TextAlign.Center,
            )
        }

        Spacer(modifier = Modifier.height(TokenSpacing.Section))

        SwitchAuthLink(
            prompt = "Já possui uma conta?",
            action = "Entrar",
            onClick = onLoginInstead,
        )
    }
}

@Composable
private fun SignupHero() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "ComparaCarros",
            style = Theme.typography.headlineLarge,
            color = Theme.colors.accentPrimary,
        )
        Spacer(modifier = Modifier.height(TokenSpacing.Item))
        Text(
            text = "Salve favoritos e acompanhe preços da FIPE.",
            style = Theme.typography.bodyMedium,
            color = Theme.colors.textSecondary,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun PasswordVisibilityToggle(
    visible: Boolean,
    onToggle: () -> Unit,
) {
    IconButton(onClick = onToggle) {
        Icon(
            imageVector = if (visible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
            contentDescription = if (visible) "Ocultar senha" else "Mostrar senha",
            tint = Theme.colors.textSecondary,
        )
    }
}

@Composable
private fun TermsRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    var showTermsDialog by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .heightIn(min = 46.dp)
                .clickable { onCheckedChange(!checked) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(checked = checked)
        Spacer(modifier = Modifier.size(TokenSpacing.Item))
        Text(
            text =
                buildAnnotatedString {
                    withStyle(SpanStyle(color = Theme.colors.textSecondary)) {
                        append("Concordo com os ")
                    }
                    withStyle(SpanStyle(color = Theme.colors.accentPrimary)) {
                        append("Termos de Uso & Privacidade")
                    }
                },
            style = Theme.typography.bodyMedium,
            modifier = Modifier.clickable { showTermsDialog = true },
        )
    }

    if (showTermsDialog) {
        TermsOfUseDialog(onDismiss = { showTermsDialog = false })
    }
}

@Composable
private fun Checkbox(checked: Boolean) {
    Box(
        modifier =
            Modifier
                .size(22.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(
                    color = if (checked) Theme.colors.accentPrimary else Theme.colors.surfaceRaised,
                    shape = RoundedCornerShape(4.dp),
                ),
        contentAlignment = Alignment.Center,
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Aceito",
                tint = Theme.colors.textInteractive,
                modifier = Modifier.size(TokenIconSize.Small),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignupScreenPreview() {
    Theme {
        SignupScreen()
    }
}
