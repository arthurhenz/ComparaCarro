package com.selectCompare

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.data.model.SmallCardData
import com.theme.Theme
import com.theme.TokenSpacing
import com.ui.FullScreenError
import com.ui.FullScreenLoading

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectComparisonScreen(
    state: SelectComparisonScreenState,
    onBackClick: () -> Unit = {},
    onCardClick: (String) -> Unit = {},
    onCompareClick: (Pair<String, String>) -> Unit = {},
    onSearchQueryChange: (String) -> Unit = {},
    onSearchFocusChanged: (Boolean) -> Unit = {},
    onToggleSelect: (String) -> Unit = {},
    onLoadMore: (Int) -> Unit = {},
) {
    Scaffold(
        containerColor = Theme.colors.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "DUELO DE MÁQUINAS",
                        style = Theme.typography.headlineLarge,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Black,
                        color = Theme.colors.textPrimary,
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Theme.colors.accentPrimary,
                        modifier =
                            Modifier
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() },
                                ) { onBackClick() }
                                .padding(TokenSpacing.Inline),
                    )
                },
                actions = {
                    val isSearchFocused = (state as? SelectComparisonScreenState.Success)?.isSearchFocused == true
                    Icon(
                        imageVector = if (isSearchFocused) Icons.Filled.Close else Icons.Filled.Search,
                        contentDescription = if (isSearchFocused) "Fechar busca" else "Buscar",
                        tint = Theme.colors.accentPrimary,
                        modifier =
                            Modifier
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() },
                                ) {
                                    if (isSearchFocused) {
                                        onSearchQueryChange("")
                                        onSearchFocusChanged(false)
                                    } else {
                                        onSearchFocusChanged(true)
                                    }
                                }
                                .padding(TokenSpacing.Inline),
                    )
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Theme.colors.surfaceGlass,
                        scrolledContainerColor = Theme.colors.surfaceGlass,
                    ),
                modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
            )
        },
    ) { paddingValues ->
        when (state) {
            is SelectComparisonScreenState.Loading -> {
                FullScreenLoading(modifier = Modifier.padding(paddingValues))
            }

            is SelectComparisonScreenState.Error -> {
                FullScreenError(
                    message = state.error,
                    modifier = Modifier.padding(paddingValues),
                )
            }

            is SelectComparisonScreenState.Success -> {
                SelectComparisonContent(
                    modifier = Modifier.padding(paddingValues),
                    state = state,
                    onCardClick = onCardClick,
                    onSearchQueryChange = onSearchQueryChange,
                    onSearchFocusChanged = onSearchFocusChanged,
                    onToggleSelect = onToggleSelect,
                    onCompareClick = onCompareClick,
                    onLoadMore = onLoadMore,
                )
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun SelectComparisonScreenPreview() {
    Theme {
        val previewCards =
            listOf(
                SmallCardData(id = "1", title = "Honda Civic", fipe = "R$ 45.000,00", selected = true),
                SmallCardData(id = "2", title = "Toyota Corolla", fipe = "R$ 50.000,00", selected = true),
                SmallCardData(id = "3", title = "Fiat Argo", fipe = "R$ 35.000,00"),
            )
        SelectComparisonContent(
            state =
                SelectComparisonScreenState.Success(
                    firstSelectedId = "1",
                    smallCards = previewCards,
                    browseCards = previewCards,
                    selectedCards = previewCards.filter { it.selected },
                    searchQuery = "",
                    isSearchFocused = false,
                ),
            onSearchQueryChange = {},
            onSearchFocusChanged = {},
            onToggleSelect = {},
            onCompareClick = {},
            onCardClick = {},
            onLoadMore = {},
        )
    }
}
