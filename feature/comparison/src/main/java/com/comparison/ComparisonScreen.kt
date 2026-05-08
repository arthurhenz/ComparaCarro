package com.comparison

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.data.R
import com.data.model.CarDetailData
import com.theme.Theme
import com.theme.TokenShapes
import com.theme.TokenSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComparisonScreen(
    state: ComparisonScreenState,
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Comparação",
                        style = Theme.typography.titleLarge,
                        color = Theme.colors.textPrimary
                    )
                },
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
            is ComparisonScreenState.Loading -> {
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

            is ComparisonScreenState.Error -> {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentState.error ?: "Unknown error",
                        style = Theme.typography.bodyLarge,
                        color = Theme.colors.error
                    )
                }
            }

            is ComparisonScreenState.Success -> {
                ComparisonContent(
                    modifier = Modifier.padding(paddingValues),
                    firstCar = currentState.firstCar,
                    secondCar = currentState.secondCar
                )
            }
        }
    }
}

@Composable
private fun ComparisonContent(
    modifier: Modifier = Modifier,
    firstCar: CarDetailData,
    secondCar: CarDetailData
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = TokenSpacing.Section, vertical = TokenSpacing.Block)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(TokenSpacing.Inline)
        ) {
            Image(
                modifier =
                    Modifier
                        .weight(1f)
                        .height(180.dp)
                        .clip(RoundedCornerShape(10.dp)),
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = firstCar.title,
                contentScale = ContentScale.Crop
            )

            Image(
                modifier =
                    Modifier
                        .weight(1f)
                        .height(180.dp)
                        .clip(RoundedCornerShape(10.dp)),
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = secondCar.title,
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(TokenSpacing.Block))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(TokenSpacing.Inline)
        ) {
            Text(
                text = firstCar.title,
                style = Theme.typography.titleLarge,
                color = Theme.colors.textPrimary,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Text(
                text = secondCar.title,
                style = Theme.typography.titleLarge,
                color = Theme.colors.textPrimary,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(TokenSpacing.Section))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = TokenShapes.Card,
            colors = CardDefaults.cardColors(containerColor = Theme.colors.surfaceLow)
        ) {
            Column(
                modifier = Modifier.padding(TokenSpacing.Block),
                verticalArrangement = Arrangement.spacedBy(TokenSpacing.Section)
            ) {
                ComparisonRow(
                    label = "Preço",
                    firstValue = firstCar.price,
                    secondValue = secondCar.price
                )

                ComparisonRow(
                    label = "Categoria",
                    firstValue = firstCar.category,
                    secondValue = secondCar.category
                )

                ComparisonRow(
                    label = "Visualizações",
                    firstValue = firstCar.views.toString(),
                    secondValue = secondCar.views.toString()
                )

                Column {
                    Text(
                        text = "Opcionais",
                        style = Theme.typography.titleLarge,
                        color = Theme.colors.textPrimary,
                        modifier = Modifier.padding(bottom = TokenSpacing.Item)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(TokenSpacing.Inline)
                    ) {
                        OptionalsColumn(
                            optionals = firstCar.optionals,
                            modifier = Modifier.weight(1f)
                        )
                        OptionalsColumn(
                            optionals = secondCar.optionals,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OptionalsColumn(
    optionals: List<String>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        if (optionals.isEmpty()) {
            Text(
                text = "Nenhum opcional",
                style = Theme.typography.bodyMedium,
                color = Theme.colors.textSecondary
            )
        } else {
            optionals.forEach { optional ->
                Text(
                    text = "• $optional",
                    style = Theme.typography.bodyMedium,
                    color = Theme.colors.textPrimary,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun ComparisonRow(
    label: String,
    firstValue: String,
    secondValue: String
) {
    Column {
        Text(
            text = label,
            style = Theme.typography.titleLarge,
            color = Theme.colors.textPrimary,
            modifier = Modifier.padding(bottom = TokenSpacing.Item)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(TokenSpacing.Inline)
        ) {
            Text(
                text = firstValue,
                style = Theme.typography.priceMedium,
                color = Theme.colors.textPrimary,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Text(
                text = secondValue,
                style = Theme.typography.priceMedium,
                color = Theme.colors.textPrimary,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ComparisonScreenPreview() {
    Theme {
        ComparisonContent(
            firstCar =
                CarDetailData(
                    id = "1",
                    title = "Honda Civic",
                    price = "R$ 45.000,00",
                    category = "SEDAN",
                    views = 150,
                    optionals = listOf("BANCO_COURO", "TETO_SOLAR", "SENSOR_RE")
                ),
            secondCar =
                CarDetailData(
                    id = "2",
                    title = "Toyota Corolla",
                    price = "R$ 50.000,00",
                    category = "SEDAN",
                    views = 200,
                    optionals = listOf("BANCO_COURO", "ALARME")
                )
        )
    }
}
