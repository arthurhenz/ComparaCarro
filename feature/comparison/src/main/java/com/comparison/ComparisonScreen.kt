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
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.data.R
import com.data.model.CarDetailData
import com.theme.ComparaCarrosTheme
import com.theme.TokenColors
import com.theme.TokenDefaultTypography
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComparisonScreen(
    firstId: String,
    secondId: String,
    onBackClick: () -> Unit = {},
    viewModel: ComparisonViewModel = koinViewModel { parametersOf(ComparisonParams(firstId, secondId)) }
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Comparação",
                        style = TokenDefaultTypography.titleLarge
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
                    CircularProgressIndicator(color = TokenColors.Primary)
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
                        style = TokenDefaultTypography.bodyLarge,
                        color = TokenColors.Error
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
                .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
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

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = firstCar.title,
                style = TokenDefaultTypography.titleMedium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Text(
                text = secondCar.title,
                style = TokenDefaultTypography.titleMedium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = TokenColors.Primary)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                ComparisonRow(
                    label = "Preço",
                    firstValue = firstCar.price,
                    secondValue = secondCar.price
                )

                Divider(modifier = Modifier.padding(vertical = 12.dp))

                ComparisonRow(
                    label = "Categoria",
                    firstValue = firstCar.category,
                    secondValue = secondCar.category
                )

                Divider(modifier = Modifier.padding(vertical = 12.dp))

                ComparisonRow(
                    label = "Visualizações",
                    firstValue = firstCar.views.toString(),
                    secondValue = secondCar.views.toString()
                )

                Divider(modifier = Modifier.padding(vertical = 12.dp))

                Text(
                    text = "Opcionais",
                    style = TokenDefaultTypography.titleSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        if (firstCar.optionals.isEmpty()) {
                            Text(
                                text = "Nenhum opcional",
                                style = TokenDefaultTypography.bodySmall,
                                color = TokenColors.Subtitle
                            )
                        } else {
                            firstCar.optionals.forEach { optional ->
                                Text(
                                    text = "• $optional",
                                    style = TokenDefaultTypography.bodySmall,
                                    modifier = Modifier.padding(vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        if (secondCar.optionals.isEmpty()) {
                            Text(
                                text = "Nenhum opcional",
                                style = TokenDefaultTypography.bodySmall,
                                color = TokenColors.Subtitle
                            )
                        } else {
                            secondCar.optionals.forEach { optional ->
                                Text(
                                    text = "• $optional",
                                    style = TokenDefaultTypography.bodySmall,
                                    modifier = Modifier.padding(vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
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
            style = TokenDefaultTypography.titleSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = firstValue,
                style = TokenDefaultTypography.bodyMedium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Text(
                text = secondValue,
                style = TokenDefaultTypography.bodyMedium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ComparisonScreenPreview() {
    ComparaCarrosTheme {
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
