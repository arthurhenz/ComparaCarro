package com.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.data.R
import com.data.model.CarAnalytics
import com.data.model.CarDetailData
import com.theme.Theme
import com.theme.TokenSpacing
import com.ui.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    state: DetailScreenState,
    onBackClick: () -> Unit = {},
    onCompareClick: (String) -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* No function */ }) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Compartilhar",
                        )
                    }
                },
                modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
            )
        },
    ) { paddingValues ->
        when (state) {
            is DetailScreenState.Loading -> {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                    contentAlignment = Alignment.Center,
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
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = state.error ?: "Unknown error",
                        style = Theme.typography.bodyLarge,
                        color = Theme.colors.error,
                    )
                }
            }

            is DetailScreenState.Success -> {
                CardDetailContent(
                    modifier = Modifier.padding(paddingValues),
                    car = state.car,
                    onCompareClick = onCompareClick,
                )
            }
        }
    }
}

@Composable
private fun CardDetailContent(
    modifier: Modifier = Modifier,
    car: CarDetailData,
    onCompareClick: (String) -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = TokenSpacing.Section)
                    .padding(bottom = 96.dp),
        ) {
            Image(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .heightIn(max = 360.dp)
                        .clip(RoundedCornerShape(10.dp)),
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "",
                contentScale = ContentScale.Crop,
            )
            // The fipeX API does not provide vehicle images.
            Text(
                text = "Imagem ilustrativa — não fornecida pela API",
                style = Theme.typography.labelMedium,
                color = Theme.colors.error,
                modifier = Modifier.padding(top = 4.dp),
            )

            Text(
                text = car.title,
                style = Theme.typography.headlineLarge,
                color = Theme.colors.textPrimary,
                modifier = Modifier.padding(top = TokenSpacing.Section),
            )
            if (car.referenceLabel.isNotBlank()) {
                Text(
                    text = "Tabela FIPE • ${car.referenceLabel}",
                    style = Theme.typography.labelMedium,
                    color = Theme.colors.textSecondary,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
            Text(
                text = car.price,
                style = Theme.typography.priceLarge,
                color = Theme.colors.accentPrimary,
                modifier = Modifier.padding(top = TokenSpacing.Item),
            )

            SectionTitle("Ficha técnica")
            DataRow("Marca", car.makeName)
            DataRow("Modelo", car.modelName)
            DataRow("Ano modelo", car.year.takeIf { it > 0 }?.toString() ?: "—")
            DataRow("Combustível", fuelLabel(car))
            DataRow("Período de referência", orDash(car.referenceLabel))
            if (car.availableYears.isNotEmpty()) {
                DataRow("Anos disponíveis", car.availableYears.joinToString(", "))
            }

            car.analytics?.let { analytics ->
                SectionTitle("Análise de preço")
                DataRow("Variação no mês", formatPct(analytics.changeFromPreviousMonthPct))
                DataRow("Variação desde o lançamento", formatPct(analytics.changeFromLaunchPct))
                DataRow("Variação desde o pico", formatPct(analytics.peakToNowPctChange))
                DataRow("Volatilidade", formatPct(analytics.priceVolatility))
                DataRow("Ranking de preço", priceRankLabel(analytics))
                DataRow("Retenção de valor", formatPct(analytics.valueRetentionPct))
                DataRow("Depreciação anual", formatPct(analytics.annualDepreciationRate))
                DataRow("Ciclo de vida", orDash(analytics.lifecycleStatus))
            }

            if (car.priceHistory.isNotEmpty()) {
                SectionTitle("Histórico de preço")
                val first = car.priceHistory.first()
                val last = car.priceHistory.last()
                DataRow("Registros", car.priceHistory.size.toString())
                DataRow("Período", "${first.month}/${first.year} – ${last.month}/${last.year}")
                DataRow("Primeiro preço", first.price)
                DataRow("Preço atual", last.price)
            }
        }

        PrimaryButton(
            text = "Comparar",
            onClick = { onCompareClick(car.id) },
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = TokenSpacing.Section),
        )
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text.uppercase(),
        style = Theme.typography.titleLarge,
        color = Theme.colors.accentPrimary,
        modifier = Modifier.padding(top = TokenSpacing.Section, bottom = TokenSpacing.Item),
    )
}

@Composable
private fun DataRow(
    label: String,
    value: String,
    fromApi: Boolean = true,
) {
    val labelColor = if (fromApi) Theme.colors.textSecondary else Theme.colors.error
    val valueColor = if (fromApi) Theme.colors.textPrimary else Theme.colors.error
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(TokenSpacing.Block),
    ) {
        Text(
            text = label,
            style = Theme.typography.labelMedium,
            color = labelColor,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = value,
            style = Theme.typography.bodyMedium,
            color = valueColor,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f),
        )
    }
}

private fun fuelLabel(car: CarDetailData): String {
    val name = car.fuelName.ifBlank { "" }
    val acronym = car.fuelAcronym.ifBlank { "" }
    return when {
        name.isNotBlank() && acronym.isNotBlank() -> "$name ($acronym)"
        name.isNotBlank() -> name
        else -> "—"
    }
}

private fun priceRankLabel(analytics: CarAnalytics): String {
    val rank = analytics.priceRank ?: return "—"
    val total = analytics.priceRankTotalInCategory
    return if (total != null) "$rank de $total" else rank.toString()
}

private fun formatPct(value: Double?): String = value?.let { "%+.2f%%".format(it * 100) } ?: "—"

private fun orDash(value: String?): String = if (value.isNullOrBlank()) "—" else value

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    Theme {
        CardDetailContent(
            car =
                CarDetailData(
                    id = "civic-hatch-dx,g,1992",
                    title = "Honda Civic Hatch DX",
                    price = "R$ 7.103,00",
                    category = "Honda",
                    views = 0,
                    optionals = emptyList(),
                    year = 1992,
                    makeName = "Honda",
                    modelName = "Civic Hatch DX",
                    fuelName = "Gasolina",
                    fuelAcronym = "g",
                    vehicleType = "carro",
                    referenceLabel = "Junho/2026",
                    analytics =
                        CarAnalytics(
                            changeFromPreviousMonthPct = -0.0019,
                            changeFromLaunchPct = -0.3065,
                            priceRank = 283,
                            priceRankTotalInCategory = 367,
                            lifecycleStatus = "active",
                            anomalyStatus = "normal",
                            anomalyZScore = -0.74,
                        ),
                    availableYears = listOf(1992, 1993, 1994, 1995),
                ),
            modifier = Modifier,
            onCompareClick = {},
        )
    }
}
