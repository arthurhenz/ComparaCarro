package com.comparacarro.navigation.routes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.comparacarro.navigation.Screen

fun NavGraphBuilder.cardDetailRoute(
    goBack: () -> Unit
) {
    composable(
        route = Screen.CardDetail.route,
        arguments = Screen.CardDetail.arguments
    ) { backStackEntry ->
        val cardId = backStackEntry.arguments?.getString("cardId") ?: ""
        CardDetailRoute(
            cardId = cardId,
            goBack = goBack
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDetailRoute(
    cardId: String,
    goBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Card Detail") },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Card Detail Screen",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "Card ID: $cardId",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
