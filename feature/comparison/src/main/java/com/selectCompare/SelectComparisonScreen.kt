package com.selectCompare

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.data.R
import com.data.model.CarDetailData
import com.theme.ComparaCarrosTheme
import com.theme.TokenColors
import com.theme.TokenDefaultTypography
import com.ui.CarDetailOptionalsList
import com.ui.OptionalItem
import com.ui.PrimaryButton
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
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is SelectComparisonScreenState.Error -> {
                Box(
                    modifier = Modifier
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
                    car = currentState.car,
                    onCompareClick = onCompareClick
                )
            }
        }
    }
}

@Composable
private fun SelectComparisonContent(
    modifier: Modifier = Modifier,
    car: CarDetailData,
    onCompareClick: (String) -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {}
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    ComparaCarrosTheme {
        SelectComparisonContent(
            car = CarDetailData(
                id = "1",
                title = "Honda Civic",
                price = "R$ 45.000,00",
                category = "SEDAN",
                views = 10,
                optionals = listOf("BANCO_COURO", "TETO_SOLAR"),
            ),
            onCompareClick = {}
        )
    }
}
