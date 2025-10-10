package com.selectCompare

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.data.model.SmallCardData
import com.theme.ComparaCarrosTheme
import com.theme.TokenColors
import com.theme.TokenDefaultTypography
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectComparisonScreen(
    firstId: String?,
    onBackClick: () -> Unit = {},
    onCardClick: (String) -> Unit,
    onCompareClick: (String) -> Unit = {},
    viewModel: SelectComparisonViewModel = koinViewModel { parametersOf(firstId ?: "") }
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        modifier =
                            Modifier
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) { onBackClick() }
                                .padding(12.dp)
                    )
                },
                actions = {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = "Compartilhar",
                        modifier =
                            Modifier
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) { /* No function */ }
                                .padding(12.dp)
                    )
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
                    CircularProgressIndicator(color = TokenColors.Primary)
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
                    onCardClick = { it -> onCardClick(it) },
                    onSearchQueryChange = viewModel::updateSearchQuery,
                    onSearchFocusChanged = viewModel::updateSearchFocus,
                    onToggleSelect = viewModel::toggleSelection,
                    onCompareClick = { secondId -> onCompareClick(secondId) }
                )
            }
        }
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
            onCompareClick = {},
            onCardClick = {}
        )
    }
}
