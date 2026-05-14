package com.comparacarro.ui.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.theme.Theme
import com.theme.TokenIconSize
import com.theme.TokenShapes
import com.theme.TokenSpacing
import com.ui.PrimaryButton

@Composable
fun SignupScreen(
    onSubmit: (name: String, email: String, password: String) -> Unit = { _, _, _ -> },
    onGoogleSignup: () -> Unit = {},
    onLoginInstead: () -> Unit = {}
) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirm by rememberSaveable { mutableStateOf("") }
    var acceptTerms by rememberSaveable { mutableStateOf(false) }

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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SignupHero()

        Spacer(modifier = Modifier.height(TokenSpacing.Section))

        AccountTextField(
            value = name,
            onValueChange = { name = it },
            label = "Nome Completo",
            placeholder = "Seu nome",
            icon = Icons.Filled.Person
        )

        Spacer(modifier = Modifier.height(TokenSpacing.Block))

        AccountTextField(
            value = email,
            onValueChange = { email = it },
            label = "E-mail",
            placeholder = "voce@email.com",
            icon = Icons.Filled.MailOutline,
            keyboardType = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(TokenSpacing.Block))

        AccountTextField(
            value = password,
            onValueChange = { password = it },
            label = "Senha",
            placeholder = "Mínimo 8 caracteres",
            icon = Icons.Filled.Lock,
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(TokenSpacing.Block))

        AccountTextField(
            value = confirm,
            onValueChange = { confirm = it },
            label = "Confirmar Senha",
            placeholder = "Repita a senha",
            icon = Icons.Filled.Shield,
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(TokenSpacing.Block))

        TermsRow(
            checked = acceptTerms,
            onCheckedChange = { acceptTerms = it }
        )

        Spacer(modifier = Modifier.height(TokenSpacing.Section))

        PrimaryButton(
            text = "CRIAR CONTA",
            enabled = acceptTerms,
            onClick = { onSubmit(name, email, password) }
        )

        Spacer(modifier = Modifier.height(TokenSpacing.Block))

        GoogleLoginButton(onClick = onGoogleSignup, text = "Cadastrar com Google")

        Spacer(modifier = Modifier.height(TokenSpacing.Section))

        SwitchAuthLink(
            prompt = "Já possui uma conta?",
            action = "Entrar",
            onClick = onLoginInstead
        )
    }
}

@Composable
private fun SignupHero() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "ComparaCarros",
            style = Theme.typography.headlineLarge,
            color = Theme.colors.accentPrimary
        )
        Spacer(modifier = Modifier.height(TokenSpacing.Item))
        Text(
            text = "Junte-se à elite automotiva brasileira",
            style = Theme.typography.bodyMedium,
            color = Theme.colors.textSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun TermsRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
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
            style = Theme.typography.bodyMedium
        )
    }
}

@Composable
private fun Checkbox(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Box(
        modifier =
            Modifier
                .size(22.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(
                    color = if (checked) Theme.colors.accentPrimary else Theme.colors.surfaceRaised,
                    shape = RoundedCornerShape(4.dp)
                )
                .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Aceito",
                tint = Theme.colors.textInteractive,
                modifier = Modifier.size(TokenIconSize.Small)
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
