package com.comparison

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.theme.TokenIconSize
import com.theme.TokenShapes
import com.theme.TokenSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComparisonScreen(
    state: ComparisonScreenState,
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        containerColor = Theme.colors.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Comparar Veículos",
                        style = Theme.typography.titleLarge,
                        color = Theme.colors.textPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Theme.colors.textPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Compartilhar",
                            tint = Theme.colors.textPrimary
                        )
                    }
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Theme.colors.background,
                        scrolledContainerColor = Theme.colors.background
                    ),
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
                AlignedComparisonContent(
                    modifier = Modifier.padding(paddingValues),
                    firstCar = currentState.firstCar,
                    secondCar = currentState.secondCar
                )
            }
        }
    }
}

@Composable
private fun AlignedComparisonContent(
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
        CarColumns(firstCar = firstCar, secondCar = secondCar)

        Spacer(modifier = Modifier.height(TokenSpacing.Section))

        SpecGroup(label = "Combustível", firstValue = "Flex", secondValue = "Turbo Flex")
        SpecGroup(label = "Motor", firstValue = firstCar.category, secondValue = secondCar.category)
        SpecGroup(label = "Portas", firstValue = "4 Portas", secondValue = "4 Portas")
        SpecGroup(
            label = "Tabela Fipe",
            firstValue = firstCar.price,
            secondValue = secondCar.price,
            valueColor = Theme.colors.accentPrimary
        )
        SpecGroup(
            label = "Visualizações",
            firstValue = firstCar.views.toString(),
            secondValue = secondCar.views.toString()
        )

        Spacer(modifier = Modifier.height(TokenSpacing.Section))

        OptionalsBlock(firstCar = firstCar, secondCar = secondCar)

        Spacer(modifier = Modifier.height(TokenSpacing.Section * 2))

        TestDriveCta(onReserve = { })

        Spacer(modifier = Modifier.height(TokenSpacing.Section))
    }
}

@Composable
private fun CarColumns(firstCar: CarDetailData, secondCar: CarDetailData) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(TokenSpacing.Block)
    ) {
        CarColumn(car = firstCar, modifier = Modifier.weight(1f))
        CarColumn(car = secondCar, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun CarColumn(
    car: CarDetailData,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(TokenShapes.Md),
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = car.title,
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(TokenSpacing.Item))
        Text(
            text = car.category,
            style = Theme.typography.labelMedium,
            color = Theme.colors.textSecondary,
            textAlign = TextAlign.Center
        )
        Text(
            text = car.title,
            style = Theme.typography.titleLarge,
            color = Theme.colors.textPrimary,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}

@Composable
private fun SpecGroup(
    label: String,
    firstValue: String,
    secondValue: String,
    valueColor: androidx.compose.ui.graphics.Color = Theme.colors.textPrimary
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = TokenSpacing.Inline)
    ) {
        Text(
            text = label,
            style = Theme.typography.labelMedium,
            color = Theme.colors.textSecondary
        )
        Spacer(modifier = Modifier.height(TokenSpacing.Item / 2))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(TokenSpacing.Block)
        ) {
            Text(
                text = firstValue,
                style = Theme.typography.priceMedium,
                color = valueColor,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Text(
                text = secondValue,
                style = Theme.typography.priceMedium,
                color = valueColor,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun OptionalsBlock(firstCar: CarDetailData, secondCar: CarDetailData) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = TokenSpacing.Inline)
    ) {
        Text(
            text = "Opcionais",
            style = Theme.typography.titleLarge,
            color = Theme.colors.textPrimary
        )
        Spacer(modifier = Modifier.height(TokenSpacing.Block))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(TokenSpacing.Block)
        ) {
            OptionalsColumn(optionals = firstCar.optionals, modifier = Modifier.weight(1f))
            OptionalsColumn(optionals = secondCar.optionals, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun OptionalsColumn(
    optionals: List<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(TokenSpacing.Item)
    ) {
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
                    color = Theme.colors.textPrimary
                )
            }
        }
    }
}

@Composable
private fun TestDriveCta(onReserve: () -> Unit) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(TokenShapes.Card)
                .background(Theme.colors.surfaceLow, shape = TokenShapes.Card)
                .padding(TokenSpacing.Block),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Pronto para acelerar?",
            style = Theme.typography.titleLarge,
            color = Theme.colors.textPrimary
        )
        Spacer(modifier = Modifier.height(TokenSpacing.Item))
        Text(
            text = "Reserve um test drive e sinta a diferença no asfalto.",
            style = Theme.typography.bodyMedium,
            color = Theme.colors.textSecondary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(TokenSpacing.Block))
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(TokenShapes.Button)
                    .background(brush = Theme.colors.interactivePrimary, shape = TokenShapes.Button)
                    .clickable(onClick = onReserve),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Reservar Test Drive",
                    style = Theme.typography.bodyLarge,
                    color = Theme.colors.textInteractive
                )
                Spacer(modifier = Modifier.width(TokenSpacing.Item))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Theme.colors.textInteractive,
                    modifier = Modifier.size(TokenIconSize.Medium)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ComparisonScreenPreview() {
    Theme {
        AlignedComparisonContent(
            firstCar =
                CarDetailData(
                    id = "1",
                    title = "Polo GTS",
                    price = "R$ 145.900",
                    category = "Volkswagen",
                    views = 150,
                    optionals = listOf("Banco de couro", "Teto solar", "Sensor de ré")
                ),
            secondCar =
                CarDetailData(
                    id = "2",
                    title = "Pulse Abarth",
                    price = "R$ 138.500",
                    category = "Fiat",
                    views = 200,
                    optionals = listOf("Banco esportivo", "Som premium")
                )
        )
    }
}
