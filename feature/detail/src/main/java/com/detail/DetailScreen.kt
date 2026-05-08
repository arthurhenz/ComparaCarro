package com.detail

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
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.data.R
import com.data.model.CarDetailData
import com.theme.Theme
import com.theme.TokenSpacing
import com.ui.CarDetailOptional
import com.ui.CarDetailOptionalsList
import com.ui.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    state: DetailScreenState,
    onBackClick: () -> Unit = {},
    onCompareClick: (String) -> Unit = {}
) {
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
        when (state) {
            is DetailScreenState.Loading -> {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Theme.colors.accentTertiary)
                }
            }

            is DetailScreenState.Error -> {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.error ?: "Unknown error",
                        style = Theme.typography.bodyLarge,
                        color = Theme.colors.error
                    )
                }
            }

            is DetailScreenState.Success -> {
                CardDetailContent(
                    modifier = Modifier.padding(paddingValues),
                    car = state.car,
                    onCompareClick = onCompareClick
                )
            }
        }
    }
}

@Composable
private fun CardDetailContent(
    modifier: Modifier = Modifier,
    car: CarDetailData,
    onCompareClick: (String) -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = TokenSpacing.Section)
                    .padding(bottom = 80.dp)
        ) {
            Image(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .heightIn(max = 360.dp)
                        .clip(RoundedCornerShape(10.dp)),
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )

            Text(
                text = car.title,
                style = Theme.typography.headlineLarge,
                color = Theme.colors.textPrimary,
                modifier = Modifier.padding(top = TokenSpacing.Section)
            )
            Text(
                text = car.price,
                style = Theme.typography.priceLarge,
                color = Theme.colors.accentPrimary,
                modifier = Modifier.padding(top = 2.dp)
            )
            if (car.optionals.isNotEmpty()) {
                val optionals = car.optionals
                CarDetailOptionalsList(
                    modifier = Modifier.padding(vertical = TokenSpacing.Inline)
                ) {
                    items(optionals) { optional ->
                        CarDetailOptional(
                            icon = painterResource(id = android.R.drawable.ic_menu_info_details),
                            title = optional
                        )
                    }
                }
            }
        }

        PrimaryButton(
            text = "Comparar",
            onClick = { onCompareClick(car.id) },
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = TokenSpacing.Section)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    Theme {
        CardDetailContent(
            car =
                CarDetailData(
                    id = "1",
                    title = "Honda Civic",
                    price = "R$ 45.000,00",
                    category = "SEDAN",
                    views = 10,
                    optionals = listOf("BANCO_COURO", "TETO_SOLAR")
                ),
            modifier = Modifier,
            onCompareClick = {}
        )
    }
}
