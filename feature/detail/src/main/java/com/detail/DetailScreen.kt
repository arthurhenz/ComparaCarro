package com.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.data.model.CarDetailData
import com.theme.ComparaCarrosTheme
import com.theme.TokenColors
import com.theme.TokenDefaultTypography
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    cardId: String,
    onBackClick: () -> Unit = {},
    onRelatedCardClick: (String) -> Unit = {},
    viewModel: DetailViewModel = koinViewModel { parametersOf(cardId) }
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Detalhes")
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
            )
        }
    ) { paddingValues ->
        when (val currentState = state) {
            is DetailScreenState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is DetailScreenState.Error -> {
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

            is DetailScreenState.Success -> {
                CardDetailContent(
                    modifier = Modifier.padding(paddingValues),
                    car = currentState.car
                )
            }
        }
    }
}

@Composable
private fun CardDetailContent(
    modifier: Modifier = Modifier,
    car: CarDetailData
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = car.title, style = TokenDefaultTypography.headlineLarge)
            Text(text = car.price, style = TokenDefaultTypography.titleLarge, color = TokenColors.HeartSelected)
            Text(text = "Categoria: " + car.category, style = TokenDefaultTypography.bodyLarge)
            Text(text = "Visualizações: " + car.views, style = TokenDefaultTypography.bodyLarge)
            if (car.optionals.isNotEmpty()) {
                Text(text = "Opcionais:", style = TokenDefaultTypography.titleMedium, modifier = Modifier.padding(top = 16.dp))
                Text(text = car.optionals.joinToString(), style = TokenDefaultTypography.bodyMedium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    ComparaCarrosTheme {
        CardDetailContent(
            car = CarDetailData(
                id = "1",
                title = "Honda Civic",
                price = "R$ 45.000,00",
                category = "SEDAN",
                views = 10,
                optionals = listOf("BANCO_COURO", "TETO_SOLAR")
            )
        )
    }
}
