package com.selectCompare

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.data.model.SmallCardData
import com.theme.ComparaCarrosTheme
import com.theme.TokenColors
import com.theme.TokenDefaultTypography
import com.ui.PrimaryButton
import com.ui.SmallCard
import com.ui.SmallCardList
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectComparisonScreen(
    firstId: String?,
    onBackClick: () -> Unit = {},
    onCompareClick: (String) -> Unit = {},
    viewModel: SelectComparisonViewModel = koinViewModel { parametersOf(firstId ?: "") }
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* No function */ }) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Compartilhar"
                        )
                    }
                },
                modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
            )
        }
    ) { paddingValues ->
        when (val currentState = state) {
            is SelectComparisonScreenState.Loading -> {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is SelectComparisonScreenState.Error -> {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentState.error ?: "Unknown error",
                        style = TokenDefaultTypography.bodyLarge,
                        color = TokenColors.Error
                    )
                }
            }

            is SelectComparisonScreenState.Success -> {
                SelectComparisonContent(
                    modifier = Modifier.padding(paddingValues),
                    state = currentState,
                    onSearchQueryChange = viewModel::updateSearchQuery,
                    onSearchFocusChanged = viewModel::updateSearchFocus,
                    onToggleSelect = viewModel::toggleSelection,
                    onCompareClick = { secondId -> onCompareClick(secondId) }
                )
            }
        }
    }
}

@Composable
private fun SelectComparisonContent(
    modifier: Modifier = Modifier,
    state: SelectComparisonScreenState.Success,
    onSearchQueryChange: (String) -> Unit,
    onSearchFocusChanged: (Boolean) -> Unit,
    onToggleSelect: (String) -> Unit,
    onCompareClick: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val selectedIds = state.allSmallCards.filter { it.selected }.map { it.id }
    val isCompareEnabled = selectedIds.size == 2

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
    ) {
        // Reuse only the search field from Header: create an inline text field copy
        com.ui.Header(
            onMenuClick = {},
            searchQuery = state.searchQuery,
            onSearchQueryChange = onSearchQueryChange,
            onSearchFocusChanged = onSearchFocusChanged,
            isSearchFocused = state.isSearchFocused
        )

        val cardHeight = 230.dp
        val verticalSpacing = 24.dp
        val numberOfRows = (state.smallCards.size + 1) / 2
        val totalHeight = (cardHeight * numberOfRows) + (verticalSpacing * (numberOfRows - 1))

        SmallCardList(
            modifier =
                Modifier
                    .padding(horizontal = 24.dp)
                    .height(totalHeight)
        ) {
            items(state.smallCards) { cardData ->
                val borderColor = if (cardData.selected) TokenColors.Primary else Color.Transparent
                Box(
                    modifier =
                        Modifier
                            .border(width = 2.dp, color = borderColor, shape = RoundedCornerShape(8.dp))
                            .clickable { onToggleSelect(cardData.id) }
                ) {
                    SmallCard(
                        image = painterResource(id = cardData.backgroundRes),
                        selected = cardData.selected,
                        onToggleButton = { _ -> onToggleSelect(cardData.id) },
                        showFavoriteToggle = false,
                        title = cardData.title,
                        fipe = cardData.fipe
                    )

                    IconButton(
                        onClick = { onToggleSelect(cardData.id) },
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        if (cardData.selected) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = "Selecionado",
                                tint = TokenColors.Primary
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.AddCircle,
                                contentDescription = "Selecionar",
                                tint = TokenColors.Subtitle
                            )
                        }
                    }
                }
            }
        }

        PrimaryButton(
            text = "Comparar",
            onClick = {
                if (selectedIds.size == 2) {
                    onCompareClick(selectedIds[1])
                }
            },
            enabled = isCompareEnabled,
            modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    ComparaCarrosTheme {
        val previewCards =
            listOf(
                SmallCardData(id = "1", title = "Honda Civic", fipe = "R$ 45.000,00", selected = true),
                SmallCardData(id = "2", title = "Toyota Corolla", fipe = "R$ 50.000,00", selected = true),
                SmallCardData(id = "3", title = "Fiat Argo", fipe = "R$ 35.000,00")
            )
        SelectComparisonContent(
            state =
                SelectComparisonScreenState.Success(
                    firstSelectedId = "1",
                    smallCards = previewCards,
                    allSmallCards = previewCards,
                    searchQuery = "",
                    isSearchFocused = false
                ),
            onSearchQueryChange = {},
            onSearchFocusChanged = {},
            onToggleSelect = {},
            onCompareClick = {}
        )
    }
}
