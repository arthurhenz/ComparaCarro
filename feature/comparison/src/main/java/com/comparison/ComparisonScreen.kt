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
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.data.model.CarAnalytics
import com.data.model.CarDetailData
import com.theme.SpaceGroteskFamily
import com.theme.Theme
import com.theme.TokenColors
import com.theme.TokenFontSizes
import com.theme.TokenIconSize
import com.theme.TokenShapes
import com.theme.TokenSpacing
import com.ui.BottomNavBar
import com.ui.BottomNavTab
import com.ui.R as UiR

private val comparisonCarImages = listOf(
    UiR.drawable.car1,
    UiR.drawable.car2,
    UiR.drawable.car3jpg,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComparisonScreen(
    state: ComparisonScreenState,
    onBackClick: () -> Unit = {},
    onNavigateToTab: (BottomNavTab) -> Unit = {},
) {
    Scaffold(
        containerColor = Theme.colors.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "VELOCIDADE",
                        style = Theme.typography.titleLarge,
                        color = Theme.colors.accentPrimary,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Theme.colors.textPrimary,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Buscar",
                            tint = Theme.colors.textPrimary,
                        )
                    }
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Theme.colors.background,
                        scrolledContainerColor = Theme.colors.background,
                    ),
                modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
            )
        },
        bottomBar = {
            BottomNavBar(
                selected = BottomNavTab.Comparar,
                onSelect = onNavigateToTab,
            )
        },
    ) { paddingValues ->
        when (val currentState = state) {
            is ComparisonScreenState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = Theme.colors.accentTertiary)
                }
            }

            is ComparisonScreenState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = currentState.error ?: "Unknown error",
                        style = Theme.typography.bodyLarge,
                        color = Theme.colors.error,
                    )
                }
            }

            is ComparisonScreenState.Success -> {
                AlignedComparisonContent(
                    modifier = Modifier.padding(paddingValues),
                    firstCar = currentState.firstCar,
                    secondCar = currentState.secondCar,
                )
            }
        }
    }
}

@Composable
private fun AlignedComparisonContent(
    modifier: Modifier = Modifier,
    firstCar: CarDetailData,
    secondCar: CarDetailData,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = TokenSpacing.Section),
    ) {
        PageHeader()

        Spacer(modifier = Modifier.height(TokenSpacing.Section))

        CarColumns(firstCar = firstCar, secondCar = secondCar)

        Spacer(modifier = Modifier.height(TokenSpacing.Section))

        SpecGroup(label = "Combustível", firstValue = fuelLabel(firstCar), secondValue = fuelLabel(secondCar))
        SpecGroup(label = "Ano modelo", firstValue = yearLabel(firstCar), secondValue = yearLabel(secondCar))
        SpecGroup(
            label = "Período de referência",
            firstValue = orDash(firstCar.referenceLabel),
            secondValue = orDash(secondCar.referenceLabel),
        )
        SpecGroup(
            label = "Tabela Fipe",
            firstValue = firstCar.price,
            secondValue = secondCar.price,
            valueColor = Theme.colors.accentPrimary,
        )
        SpecGroup(
            label = "Variação no mês",
            firstValue = formatPct(firstCar.analytics?.changeFromPreviousMonthPct),
            secondValue = formatPct(secondCar.analytics?.changeFromPreviousMonthPct),
        )
        SpecGroup(
            label = "Variação desde o lançamento",
            firstValue = formatPct(firstCar.analytics?.changeFromLaunchPct),
            secondValue = formatPct(secondCar.analytics?.changeFromLaunchPct),
        )
        SpecGroup(
            label = "Volatilidade",
            firstValue = formatPct(firstCar.analytics?.priceVolatility),
            secondValue = formatPct(secondCar.analytics?.priceVolatility),
        )
        SpecGroup(
            label = "Ranking de preço",
            firstValue = priceRankLabel(firstCar.analytics),
            secondValue = priceRankLabel(secondCar.analytics),
        )
        SpecGroup(
            label = "Depreciação anual",
            firstValue = formatPct(firstCar.analytics?.annualDepreciationRate),
            secondValue = formatPct(secondCar.analytics?.annualDepreciationRate),
        )
        SpecGroup(
            label = "Ciclo de vida",
            firstValue = orDash(firstCar.analytics?.lifecycleStatus),
            secondValue = orDash(secondCar.analytics?.lifecycleStatus),
        )

        Spacer(modifier = Modifier.height(TokenSpacing.Section))

        TestDriveCta(onReserve = { })

        Spacer(modifier = Modifier.height(TokenSpacing.Section))
    }
}

@Composable
private fun PageHeader() {
    Column {
        Text(
            text = "Performance & Engineering".uppercase(),
            style =
                TextStyle(
                    fontFamily = SpaceGroteskFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = TokenFontSizes.Medium,
                ),
            color = Theme.colors.accentPrimary,
            modifier = Modifier.padding(bottom = 4.dp),
        )
        Text(
            text = "Duelo de".uppercase(),
            style = Theme.typography.headlineLarge,
            fontStyle = FontStyle.Italic,
            fontSize = 56.sp,
            color = Theme.colors.textPrimary,
        )
        Text(
            text = "Máquinas".uppercase(),
            style = Theme.typography.headlineLarge,
            fontStyle = FontStyle.Italic,
            fontSize = 56.sp,
            color = Theme.colors.textPrimary,
        )
        Spacer(
            modifier =
                Modifier
                    .padding(top = 4.dp)
                    .height(4.dp)
                    .width(156.dp)
                    .background(color = TokenColors.PrimaryAccent),
        )
    }
}

@Composable
private fun CarColumns(firstCar: CarDetailData, secondCar: CarDetailData) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(TokenSpacing.Block),
    ) {
        CarColumn(
            car = firstCar,
            imageRes = comparisonCarImages[0],
            modifier = Modifier.weight(1f),
        )
        CarColumn(
            car = secondCar,
            imageRes = comparisonCarImages[1],
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun CarColumn(
    car: CarDetailData,
    imageRes: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(TokenShapes.Md),
            painter = painterResource(id = imageRes),
            contentDescription = car.title,
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.height(TokenSpacing.Item))
        Text(
            text = car.title.uppercase(),
            style = Theme.typography.titleLarge,
            fontStyle = FontStyle.Italic,
            color = Theme.colors.textPrimary,
            textAlign = TextAlign.Center,
            maxLines = 2,
        )
        Text(
            text = car.category.uppercase(),
            style = Theme.typography.labelMedium,
            color = Theme.colors.accentPrimary,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun SpecGroup(
    label: String,
    firstValue: String,
    secondValue: String,
    valueColor: androidx.compose.ui.graphics.Color = Theme.colors.textPrimary,
    fromApi: Boolean = true,
) {
    val resolvedLabelColor = if (fromApi) Theme.colors.textSecondary else Theme.colors.error
    val resolvedValueColor = if (fromApi) valueColor else Theme.colors.error
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = TokenSpacing.Inline),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = if (fromApi) label else "$label (não fornecido pela API)",
            style = Theme.typography.labelMedium,
            color = resolvedLabelColor,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(TokenSpacing.Item))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(TokenSpacing.Block),
        ) {
            Text(
                text = firstValue,
                style = Theme.typography.priceMedium,
                color = resolvedValueColor,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
            )
            Text(
                text = secondValue,
                style = Theme.typography.priceMedium,
                color = resolvedValueColor,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
            )
        }
    }
}

private fun fuelLabel(car: CarDetailData): String {
    val name = car.fuelName
    val acronym = car.fuelAcronym
    return when {
        name.isNotBlank() && acronym.isNotBlank() -> "$name ($acronym)"
        name.isNotBlank() -> name
        else -> "—"
    }
}

private fun yearLabel(car: CarDetailData): String = car.year.takeIf { it > 0 }?.toString() ?: "—"

private fun priceRankLabel(analytics: CarAnalytics?): String {
    val rank = analytics?.priceRank ?: return "—"
    val total = analytics.priceRankTotalInCategory
    return if (total != null) "$rank/$total" else rank.toString()
}

private fun formatPct(value: Double?): String = value?.let { "%+.2f%%".format(it * 100) } ?: "—"

private fun orDash(value: String?): String = if (value.isNullOrBlank()) "—" else value

@Composable
private fun TestDriveCta(onReserve: () -> Unit) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(TokenShapes.Card)
                .background(Theme.colors.surfaceLow, shape = TokenShapes.Card)
                .padding(TokenSpacing.Block),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = "Pronto para acelerar?".uppercase(),
            style = Theme.typography.titleLarge,
            fontStyle = FontStyle.Italic,
            color = Theme.colors.accentPrimary,
        )
        Spacer(modifier = Modifier.height(TokenSpacing.Item))
        Text(
            text =
                "Nossa equipe técnica pode te ajudar a montar uma análise detalhada " +
                    "do carro ideal em nossa concessionária.",
            style = Theme.typography.bodyMedium,
            color = Theme.colors.textSecondary,
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
            contentAlignment = Alignment.Center,
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Reservar Test Drive".uppercase(),
                    style = Theme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Theme.colors.textInteractive,
                )
                Spacer(modifier = Modifier.width(TokenSpacing.Item))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Theme.colors.textInteractive,
                    modifier = Modifier.size(TokenIconSize.Medium),
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
                    optionals = listOf("Banco de couro", "Teto solar", "Sensor de ré"),
                ),
            secondCar =
                CarDetailData(
                    id = "2",
                    title = "Pulse Abarth",
                    price = "R$ 138.500",
                    category = "Fiat",
                    views = 200,
                    optionals = listOf("Banco esportivo", "Som premium"),
                ),
        )
    }
}
