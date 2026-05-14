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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.theme.Theme
import com.theme.TokenIconSize
import com.theme.TokenShapes
import com.theme.TokenSpacing
import com.ui.BottomNavBar
import com.ui.BottomNavTab

data class ProfileUser(
    val name: String,
    val email: String,
    val phone: String,
    val duels: Int,
    val favorites: Int,
    val level: String,
    val garage: Int
)

data class GarageEntry(val id: String, val name: String)

data class DuelHistoryEntry(val id: String, val title: String, val date: String)

@Composable
fun ProfileScreen(
    user: ProfileUser = sampleUser(),
    favoriteCars: List<GarageEntry> = sampleGarage(),
    duels: List<DuelHistoryEntry> = sampleDuels(),
    onEditProfile: () -> Unit = {},
    onSeeAllFavorites: () -> Unit = {},
    onDuelClick: (String) -> Unit = {},
    onLogout: () -> Unit = {},
    onNavigate: (BottomNavTab) -> Unit = {}
) {
    var selectedTab by rememberSaveable { mutableStateOf(BottomNavTab.Perfil) }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Theme.colors.background)
                .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = TokenSpacing.Section, vertical = TokenSpacing.Block)
        ) {
            ProfileHeader(user = user, onEdit = onEditProfile)

            Spacer(modifier = Modifier.height(TokenSpacing.Section))

            StatsRow(user = user)

            Spacer(modifier = Modifier.height(TokenSpacing.Section))

            SectionHeader(
                title = "Meus Carros Favoritos",
                action = "VER TODOS",
                onActionClick = onSeeAllFavorites
            )
            Spacer(modifier = Modifier.height(TokenSpacing.Block))
            GarageRow(entries = favoriteCars)

            Spacer(modifier = Modifier.height(TokenSpacing.Section))

            SectionHeader(title = "Histórico de Duelos")
            Spacer(modifier = Modifier.height(TokenSpacing.Block))
            duels.forEach { duel ->
                DuelEntryRow(
                    duel = duel,
                    onClick = { onDuelClick(duel.id) }
                )
                Spacer(modifier = Modifier.height(TokenSpacing.Item))
            }

            Spacer(modifier = Modifier.height(TokenSpacing.Section))

            LogoutButton(onClick = onLogout)

            Spacer(modifier = Modifier.height(TokenSpacing.Section))
        }

        BottomNavBar(
            selected = selectedTab,
            onSelect = { tab ->
                selectedTab = tab
                onNavigate(tab)
            }
        )
    }
}

@Composable
private fun ProfileHeader(user: ProfileUser, onEdit: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier =
                Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(brush = Theme.colors.interactivePrimary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = null,
                tint = Theme.colors.textInteractive,
                modifier = Modifier.size(48.dp)
            )
        }
        Spacer(modifier = Modifier.height(TokenSpacing.Block))
        Text(
            text = user.name,
            style = Theme.typography.headlineLarge,
            color = Theme.colors.textPrimary
        )
        Spacer(modifier = Modifier.height(TokenSpacing.Item))
        ContactRow(icon = Icons.Filled.Email, text = user.email)
        Spacer(modifier = Modifier.height(TokenSpacing.Item / 2))
        ContactRow(icon = Icons.Filled.Phone, text = user.phone)
        Spacer(modifier = Modifier.height(TokenSpacing.Block))
        EditProfileButton(onClick = onEdit)
    }
}

@Composable
private fun ContactRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Theme.colors.textSecondary,
            modifier = Modifier.size(TokenIconSize.Small)
        )
        Spacer(modifier = Modifier.width(TokenSpacing.Item))
        Text(
            text = text,
            style = Theme.typography.bodyMedium,
            color = Theme.colors.textSecondary
        )
    }
}

@Composable
private fun EditProfileButton(onClick: () -> Unit) {
    Row(
        modifier =
            Modifier
                .clip(TokenShapes.Button)
                .background(Theme.colors.surfaceRaised, shape = TokenShapes.Button)
                .clickable(onClick = onClick)
                .padding(horizontal = TokenSpacing.Block, vertical = TokenSpacing.Item),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Edit,
            contentDescription = null,
            tint = Theme.colors.accentPrimary,
            modifier = Modifier.size(TokenIconSize.Small)
        )
        Spacer(modifier = Modifier.width(TokenSpacing.Item))
        Text(
            text = "Editar Perfil",
            style = Theme.typography.labelMedium,
            color = Theme.colors.accentPrimary
        )
    }
}

@Composable
private fun StatsRow(user: ProfileUser) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(TokenShapes.Card)
                .background(Theme.colors.surfaceLow, shape = TokenShapes.Card)
                .padding(vertical = TokenSpacing.Block),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatBlock(value = user.duels.toString(), label = "Duelos")
        StatBlock(value = user.favorites.toString(), label = "Favoritos")
        StatBlock(value = user.level, label = "Nível", accent = true)
        StatBlock(value = user.garage.toString(), label = "Garagem")
    }
}

@Composable
private fun StatBlock(value: String, label: String, accent: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = Theme.typography.headlineLarge,
            color = if (accent) Theme.colors.accentPrimary else Theme.colors.textPrimary
        )
        Text(
            text = label,
            style = Theme.typography.labelMedium,
            color = Theme.colors.textSecondary
        )
    }
}

@Composable
private fun SectionHeader(
    title: String,
    action: String? = null,
    onActionClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = Theme.typography.titleLarge,
            color = Theme.colors.textPrimary
        )
        if (action != null) {
            Text(
                text = action,
                style = Theme.typography.labelMedium,
                color = Theme.colors.accentPrimary,
                modifier = Modifier.clickable(onClick = onActionClick)
            )
        }
    }
}

@Composable
private fun GarageRow(entries: List<GarageEntry>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(TokenSpacing.Block)
    ) {
        items(entries) { entry ->
            GarageItem(entry)
        }
    }
}

@Composable
private fun GarageItem(entry: GarageEntry) {
    Column(
        modifier = Modifier.width(120.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier =
                Modifier
                    .size(width = 120.dp, height = 80.dp)
                    .clip(TokenShapes.Card)
                    .background(Theme.colors.surfaceRaised, shape = TokenShapes.Card)
        )
        Spacer(modifier = Modifier.height(TokenSpacing.Item))
        Text(
            text = entry.name,
            style = Theme.typography.labelMedium,
            color = Theme.colors.textPrimary,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}

@Composable
private fun DuelEntryRow(duel: DuelHistoryEntry, onClick: () -> Unit) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(TokenShapes.Card)
                .background(Theme.colors.surfaceLow, shape = TokenShapes.Card)
                .clickable(onClick = onClick)
                .padding(TokenSpacing.Block),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = duel.title,
                style = Theme.typography.bodyLarge,
                color = Theme.colors.textPrimary
            )
            Text(
                text = duel.date,
                style = Theme.typography.labelMedium,
                color = Theme.colors.textSecondary
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = Theme.colors.textSecondary,
            modifier = Modifier.size(TokenIconSize.Small)
        )
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
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Logout,
            contentDescription = null,
            tint = Theme.colors.error,
            modifier = Modifier.size(TokenIconSize.Medium)
        )
        Spacer(modifier = Modifier.width(TokenSpacing.Item))
        Text(
            text = "Sair da conta",
            style = Theme.typography.bodyLarge,
            color = Theme.colors.error
        )
    }
}

private fun sampleUser(): ProfileUser = ProfileUser(
    name = "Carlos Silva",
    email = "carlos.silva@email.com.br",
    phone = "+55 11 99876-5432",
    duels = 142,
    favorites = 28,
    level = "PRO",
    garage = 12
)

private fun sampleGarage(): List<GarageEntry> = listOf(
    GarageEntry("g1", "Porsche 911 GT3 RS"),
    GarageEntry("g2", "McLaren 720S Spider"),
    GarageEntry("g3", "BMW M3 Competition"),
    GarageEntry("g4", "Ferrari SF90 Stradale")
)

private fun sampleDuels(): List<DuelHistoryEntry> = listOf(
    DuelHistoryEntry("d1", "Polo GTS vs Pulse Abarth", "Ontem"),
    DuelHistoryEntry("d2", "Civic Si vs Corolla XEi", "3 de outubro"),
    DuelHistoryEntry("d3", "M3 vs C63 AMG", "28 de setembro")
)

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    Theme {
        ProfileScreen()
    }
}
