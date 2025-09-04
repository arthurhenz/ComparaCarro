package com.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.data.model.LargeCardData
import com.data.model.SmallCardData
import com.theme.ComparaCarrosTheme
import com.theme.TokenColors
import com.ui.Header
import com.ui.LargeCard
import com.ui.LargeCardCarousel
import com.ui.PrimaryButton
import com.ui.SmallCard

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    
    when (val currentState = state) {
        is HomeScreenState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is HomeScreenState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currentState.error ?: "Unknown error",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        is HomeScreenState.Success -> {
            HomeContent(
                largeCards = currentState.largeCards,
                smallCards = currentState.smallCards
            )
        }
    }
}

@Composable
private fun HomeContent(
    largeCards: List<LargeCardData>,
    smallCards: List<SmallCardData>
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Header(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.statusBars),
            onMenuClick = {}
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(top = 56.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LargeCardCarousel(modifier = Modifier.padding(bottom = 24.dp)) {
                largeCards.forEach { cardData ->
                    item {
                        LargeCard(
                            background = painterResource(id = cardData.backgroundRes),
                            title = cardData.title
                        )
                    }
                }
            }

            PrimaryButton(
                text = "Comparar",
                onClick = {},
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp, horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mais populares",
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = "Ver todos",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TokenColors.Subtitle
                )
            }

            smallCards.chunked(2).forEachIndexed { index, rowCards ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 24.dp,
                            vertical = if (index == 0) 0.dp else 12.dp
                        ),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    rowCards.forEach { cardData ->
                        SmallCard(
                            modifier = Modifier.weight(1f),
                            background = painterResource(id = cardData.backgroundRes),
                            selected = cardData.selected,
                            onToggleButton = {},
                            title = cardData.title,
                            price = cardData.price
                        )
                    }

                    if (rowCards.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ComparaCarrosTheme {
        HomeContent(
            largeCards = listOf(
                LargeCardData(
                    id = "preview_large_1",
                    title = "Novidades do mês"
                ),
                LargeCardData(
                    id = "preview_large_2",
                    title = "Ofertas imperdíveis"
                )
            ),
            smallCards = listOf(
                SmallCardData(
                    id = "preview_small_1",
                    title = "Volkswagen Saveiro 2017",
                    price = "R$ 55.900",
                    selected = true
                ),
                SmallCardData(
                    id = "preview_small_2",
                    title = "Audi A4 Quattro Sedan 2019",
                    price = "R$ 142.000",
                    selected = false
                ),
                SmallCardData(
                    id = "preview_small_3",
                    title = "Honda Civic Si LX LXS 2020",
                    price = "R$ 115.500",
                    selected = false
                ),
                SmallCardData(
                    id = "preview_small_4",
                    title = "Toyota Corolla Xei Guerra Corolla Siria 2021",
                    price = "R$ 128.000",
                    selected = true
                )
            )
        )
    }
}
