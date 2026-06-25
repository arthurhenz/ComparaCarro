package com.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import com.theme.Theme
import com.theme.TokenIconSize
import com.theme.TokenSpacing

@Composable
fun Header(
    modifier: Modifier = Modifier,
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {},
    onSearchFocusChanged: (Boolean) -> Unit = {},
    isSearchFocused: Boolean = false,
    title: String = "",
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .background(Theme.colors.surfaceHeader),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = title.uppercase(),
                style = Theme.typography.headlineLarge,
                fontStyle = FontStyle.Italic,
                color = Theme.colors.accentPrimary,
                modifier = Modifier.padding(start = TokenSpacing.Block),
            )

            IconButton(
                onClick = {
                    if (isSearchFocused) {
                        onSearchQueryChange("")
                        focusManager.clearFocus()
                        onSearchFocusChanged(false)
                    } else {
                        onSearchFocusChanged(true)
                    }
                },
            ) {
                Icon(
                    imageVector = if (isSearchFocused) Icons.Filled.Close else Icons.Filled.Search,
                    contentDescription = if (isSearchFocused) "Fechar busca" else "Buscar",
                    tint = Theme.colors.accentPrimary,
                    modifier = Modifier.size(TokenIconSize.Large),
                )
            }
        }

        AnimatedVisibility(visible = isSearchFocused) {
            SearchField(
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                onSearchFocusChanged = onSearchFocusChanged,
                isSearchFocused = isSearchFocused,
            )
        }
    }
}

@Composable
fun Header(
    modifier: Modifier = Modifier,
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {},
    onSearchFocusChanged: (Boolean) -> Unit = {},
    isSearchFocused: Boolean = false,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .background(Theme.colors.surfaceHeader),
    ) {
        SearchField(
            searchQuery = searchQuery,
            onSearchQueryChange = onSearchQueryChange,
            onSearchFocusChanged = onSearchFocusChanged,
            isSearchFocused = isSearchFocused,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderPreview() {
    Theme {
        Header(
            searchQuery = "",
            onSearchQueryChange = {},
            onSearchFocusChanged = {},
            isSearchFocused = false,
            title = "COMPARA CARROS",
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderSearchActivePreview() {
    Theme {
        Header(
            searchQuery = "Civic",
            onSearchQueryChange = {},
            onSearchFocusChanged = {},
            isSearchFocused = true,
            title = "COMPARA CARROS",
        )
    }
}
