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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.theme.Theme
import com.theme.TokenIconSize
import com.theme.TokenShapes
import com.theme.TokenSpacing
import com.ui.PrimaryButton

@Composable
fun LoginScreen(
    onSubmit: (email: String, password: String) -> Unit = { _, _ -> },
    onGoogleLogin: () -> Unit = {},
    onForgotPassword: () -> Unit = {},
    onCreateAccount: () -> Unit = {}
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

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
        BrandHero()

        Spacer(modifier = Modifier.height(TokenSpacing.Section))

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
            placeholder = "Sua senha",
            icon = Icons.Filled.Lock,
            keyboardType = KeyboardType.Password,
            visualTransformation =
                if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector =
                            if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription =
                            if (passwordVisible) "Ocultar senha" else "Mostrar senha",
                        tint = Theme.colors.textSecondary
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(TokenSpacing.Item))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Esqueci minha senha",
                style = Theme.typography.labelMedium,
                color = Theme.colors.accentPrimary,
                modifier = Modifier.clickable(onClick = onForgotPassword)
            )
        }

        Spacer(modifier = Modifier.height(TokenSpacing.Section))

        PrimaryButton(
            text = "ENTRAR",
            onClick = { onSubmit(email, password) }
        )

        Spacer(modifier = Modifier.height(TokenSpacing.Block))

        GoogleLoginButton(onClick = onGoogleLogin)

        Spacer(modifier = Modifier.height(TokenSpacing.Section))

        SwitchAuthLink(
            prompt = "Não tem uma conta?",
            action = "Criar conta",
            onClick = onCreateAccount
        )

        Spacer(modifier = Modifier.height(TokenSpacing.Section))

        LoginFooter()
    }
}

@Composable
private fun BrandHero() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier =
                Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(brush = Theme.colors.interactivePrimary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.DirectionsCar,
                contentDescription = null,
                tint = Theme.colors.textInteractive,
                modifier = Modifier.size(48.dp)
            )
        }
        Spacer(modifier = Modifier.height(TokenSpacing.Block))
        Text(
            text = "ComparaCarros",
            style = Theme.typography.headlineLarge,
            color = Theme.colors.accentPrimary
        )
        Spacer(modifier = Modifier.height(TokenSpacing.Item / 2))
        Text(
            text = "HIGH PERFORMANCE HUB",
            style = Theme.typography.labelMedium,
            color = Theme.colors.textSecondary
        )
    }
}

@Composable
internal fun AccountTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    icon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: (@Composable () -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(
                text = label,
                style = Theme.typography.labelMedium,
                color = Theme.colors.textSecondary
            )
        },
        placeholder = {
            Text(
                text = placeholder,
                style = Theme.typography.bodyMedium,
                color = Theme.colors.textSecondary
            )
        },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Theme.colors.textSecondary,
                modifier = Modifier.size(TokenIconSize.Medium)
            )
        },
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = visualTransformation,
        singleLine = true,
        shape = TokenShapes.Sm,
        colors =
            TextFieldDefaults.colors(
                focusedContainerColor = Theme.colors.surfaceInput,
                unfocusedContainerColor = Theme.colors.surfaceInput,
                focusedIndicatorColor = Theme.colors.accentPrimary,
                unfocusedIndicatorColor = Theme.colors.outlineGhost,
                focusedTextColor = Theme.colors.textPrimary,
                unfocusedTextColor = Theme.colors.textPrimary,
                focusedLabelColor = Theme.colors.accentPrimary,
                unfocusedLabelColor = Theme.colors.textSecondary
            )
    )
}

@Composable
internal fun GoogleLoginButton(onClick: () -> Unit, text: String = "Continuar com Google") {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = TokenSpacing.Block)
                .height(54.dp)
                .clip(TokenShapes.Button)
                .background(Theme.colors.surfaceRaised, shape = TokenShapes.Button)
                .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier =
                    Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Theme.colors.textPrimary, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "G",
                    style = Theme.typography.labelMedium,
                    color = Theme.colors.accentPrimary
                )
            }
            Spacer(modifier = Modifier.width(TokenSpacing.Block))
            Text(
                text = text,
                style = Theme.typography.bodyLarge,
                color = Theme.colors.textPrimary
            )
        }
    }
}

@Composable
internal fun SwitchAuthLink(
    prompt: String,
    action: String,
    onClick: () -> Unit
) {
    val annotated: AnnotatedString =
        buildAnnotatedString {
            withStyle(
                SpanStyle(color = Theme.colors.textSecondary)
            ) { append("$prompt ") }
            withStyle(
                SpanStyle(color = Theme.colors.accentPrimary)
            ) { append(action) }
        }
    Text(
        text = annotated,
        style = Theme.typography.bodyMedium,
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Composable
private fun LoginFooter() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Privacidade",
            style = Theme.typography.labelMedium,
            color = Theme.colors.textSecondary
        )
        DotSeparator()
        Text(
            text = "Termos",
            style = Theme.typography.labelMedium,
            color = Theme.colors.textSecondary
        )
        DotSeparator()
        Text(
            text = "v.2.4.0 ENGINE",
            style = Theme.typography.labelMedium,
            color = Theme.colors.textSecondary
        )
    }
}

@Composable
private fun DotSeparator() {
    Box(
        modifier =
            Modifier
                .padding(horizontal = TokenSpacing.Item)
                .size(3.dp)
                .clip(CircleShape)
                .background(Theme.colors.outlineGhost, shape = CircleShape)
    )
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    Theme {
        LoginScreen()
    }
}
