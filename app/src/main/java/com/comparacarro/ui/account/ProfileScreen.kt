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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.theme.Theme
import com.theme.TokenIconSize
import com.theme.TokenShapes
import com.theme.TokenSpacing
import com.ui.BottomNavBar
import com.ui.BottomNavTab

@Composable
fun ProfileScreen(
    name: String,
    email: String,
    onLogout: () -> Unit = {},
    onNavigate: (BottomNavTab) -> Unit = {},
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Theme.colors.background)
                .windowInsetsPadding(WindowInsets.statusBars),
    ) {
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = TokenSpacing.Section, vertical = TokenSpacing.Block),
        ) {
            ProfileHeader(name = name, email = email)

            Spacer(modifier = Modifier.height(TokenSpacing.Section))

            LogoutButton(onClick = onLogout)
        }

        BottomNavBar(
            selected = BottomNavTab.Perfil,
            onSelect = onNavigate,
        )
    }
}

@Composable
private fun ProfileHeader(name: String, email: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier =
                Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(brush = Theme.colors.interactivePrimary),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = null,
                tint = Theme.colors.textInteractive,
                modifier = Modifier.size(48.dp),
            )
        }
        Spacer(modifier = Modifier.height(TokenSpacing.Block))
        Text(
            text = name,
            style = Theme.typography.headlineLarge,
            color = Theme.colors.textPrimary,
        )
        if (email.isNotBlank()) {
            Spacer(modifier = Modifier.height(TokenSpacing.Item))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = null,
                    tint = Theme.colors.textSecondary,
                    modifier = Modifier.size(TokenIconSize.Small),
                )
                Spacer(modifier = Modifier.width(TokenSpacing.Item))
                Text(
                    text = email,
                    style = Theme.typography.bodyMedium,
                    color = Theme.colors.textSecondary,
                )
            }
        }
    }
}

@Composable
private fun LogoutButton(onClick: () -> Unit) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(TokenShapes.Button)
                .background(Theme.colors.surfaceRaised, shape = TokenShapes.Button)
                .clickable(onClick = onClick)
                .padding(vertical = TokenSpacing.Block),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Logout,
            contentDescription = null,
            tint = Theme.colors.error,
            modifier = Modifier.size(TokenIconSize.Medium),
        )
        Spacer(modifier = Modifier.width(TokenSpacing.Item))
        Text(
            text = "Sair da conta",
            style = Theme.typography.bodyLarge,
            color = Theme.colors.error,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    Theme {
        ProfileScreen(
            name = "Carlos Silva",
            email = "carlos.silva@email.com.br",
        )
    }
}
