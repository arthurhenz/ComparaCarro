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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import com.ui.rememberCarImagePainter

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
                    Column {
                        Text(
                            text = "Duelo",
                            style = Theme.typography.titleLarge,
                            color = Theme.colors.textPrimary,
                        )
                        Text(
                            text = "Comparação Técnica".uppercase(),
                            style = Theme.typography.labelMedium,
                            color = Theme.colors.textSecondary,
                        )
                    }
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
        LaunchVariationSpecGroup(
            firstPct = firstCar.analytics?.changeFromLaunchPct,
            secondPct = secondCar.analytics?.changeFromLaunchPct,
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

        VerdictCard(firstCar = firstCar, secondCar = secondCar)

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
            modifier = Modifier.weight(1f),
        )
        CarColumn(
            car = secondCar,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun CarColumn(
    car: CarDetailData,
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
            painter = rememberCarImagePainter(car.imageUrl),
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

/**
 * Same layout as [SpecGroup], but for "Variação desde o lançamento" the car with the smaller
 * variation is highlighted green with an upward chevron on its outer edge, and the car with the
 * larger variation is highlighted red with a downward chevron on its opposite outer edge.
 */
@Composable
private fun LaunchVariationSpecGroup(firstPct: Double?, secondPct: Double?) {
    val firstIsLower = firstPct != null && secondPct != null && firstPct < secondPct
    val firstIsHigher = firstPct != null && secondPct != null && firstPct > secondPct

    val firstColor =
        when {
            firstIsLower -> TokenColors.Success
            firstIsHigher -> Theme.colors.error
            else -> Theme.colors.textPrimary
        }
    val secondColor =
        when {
            firstIsLower -> Theme.colors.error
            firstIsHigher -> TokenColors.Success
            else -> Theme.colors.textPrimary
        }

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = TokenSpacing.Inline),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Variação desde o lançamento",
            style = Theme.typography.labelMedium,
            color = Theme.colors.textSecondary,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(TokenSpacing.Item))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (firstIsLower) {
                VariationChevron(up = true, color = TokenColors.Success)
            } else if (firstIsHigher) {
                VariationChevron(up = false, color = Theme.colors.error)
            }
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(TokenSpacing.Block),
            ) {
                Text(
                    text = formatPct(firstPct),
                    style = Theme.typography.priceMedium,
                    color = firstColor,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = formatPct(secondPct),
                    style = Theme.typography.priceMedium,
                    color = secondColor,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                )
            }
            if (firstIsLower) {
                VariationChevron(up = false, color = Theme.colors.error)
            } else if (firstIsHigher) {
                VariationChevron(up = true, color = TokenColors.Success)
            }
        }
    }
}

@Composable
private fun VariationChevron(up: Boolean, color: androidx.compose.ui.graphics.Color) {
    Icon(
        imageVector = if (up) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
        contentDescription = null,
        tint = color,
        modifier = Modifier.size(TokenIconSize.Medium),
    )
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

private fun parseBrl(price: String): Double? =
    price.replace(Regex("[^0-9,]"), "").replace(",", ".").toDoubleOrNull()

/**
 * Counts, among the numeric criteria shown on this screen, how many favor each car — lower price,
 * lower volatility and lower annual depreciation are the "wins" (higher launch/monthly variation is
 * a loss, since it means the price has risen more).
 */
private fun countWins(firstCar: CarDetailData, secondCar: CarDetailData): Pair<Int, Int> {
    val comparisons =
        listOf(
            parseBrl(firstCar.price) to parseBrl(secondCar.price),
            firstCar.analytics?.changeFromPreviousMonthPct to secondCar.analytics?.changeFromPreviousMonthPct,
            firstCar.analytics?.changeFromLaunchPct to secondCar.analytics?.changeFromLaunchPct,
            firstCar.analytics?.priceVolatility to secondCar.analytics?.priceVolatility,
            firstCar.analytics?.annualDepreciationRate to secondCar.analytics?.annualDepreciationRate,
        )
    var firstWins = 0
    var secondWins = 0
    comparisons.forEach { (first, second) ->
        if (first != null && second != null && first != second) {
            if (first < second) firstWins++ else secondWins++
        }
    }
    return firstWins to secondWins
}

@Composable
private fun VerdictCard(firstCar: CarDetailData, secondCar: CarDetailData) {
    val (firstWins, secondWins) = countWins(firstCar, secondCar)
    val total = firstWins + secondWins
    val winner =
        when {
            firstWins > secondWins -> firstCar
            secondWins > firstWins -> secondCar
            else -> null
        }
    val wins = maxOf(firstWins, secondWins)

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(TokenShapes.Card)
                .background(Theme.colors.surfaceLow, shape = TokenShapes.Card)
                .padding(TokenSpacing.Block),
    ) {
        Text(
            text = "Veredito".uppercase(),
            style = Theme.typography.labelMedium,
            color = Theme.colors.accentPrimary,
        )
        Spacer(modifier = Modifier.height(TokenSpacing.Item))
        Text(
            text =
                if (winner != null) {
                    "${winner.title} vence em $wins de $total critérios."
                } else {
                    "Empate técnico entre os dois modelos."
                },
            style = Theme.typography.titleLarge,
            color = Theme.colors.textPrimary,
        )
        Spacer(modifier = Modifier.height(TokenSpacing.Item))
        Text(
            text = "Critérios considerados: preço, variação no mês, desde o lançamento, volatilidade e depreciação anual.",
            style = Theme.typography.bodyMedium,
            color = Theme.colors.textSecondary,
        )
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
