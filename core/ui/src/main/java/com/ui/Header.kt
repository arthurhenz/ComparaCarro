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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import com.theme.Theme
import com.theme.TokenIconSize
import com.theme.TokenShapes
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
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(isSearchFocused) {
        if (isSearchFocused) focusRequester.requestFocus()
    }

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
                focusRequester = focusRequester,
                focusManager = focusManager,
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
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

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
            focusRequester = focusRequester,
            focusManager = focusManager,
        )
    }
}

@Composable
private fun SearchField(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchFocusChanged: (Boolean) -> Unit,
    isSearchFocused: Boolean,
    focusRequester: FocusRequester,
    focusManager: androidx.compose.ui.focus.FocusManager,
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    start = TokenSpacing.Block,
                    end = TokenSpacing.Block,
                    top = TokenSpacing.Item,
                    bottom = TokenSpacing.Block,
                )
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    onSearchFocusChanged(focusState.isFocused)
                },
        placeholder = {
            Text(
                text = "Buscar por modelo...",
                style = Theme.typography.bodyMedium,
                color = Theme.colors.textSecondary,
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                tint = Theme.colors.textSecondary,
            )
        },
        trailingIcon = {
            if (isSearchFocused) {
                IconButton(
                    onClick = {
                        onSearchQueryChange("")
                        focusManager.clearFocus()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Clear and unfocus",
                        tint = Theme.colors.textSecondary,
                    )
                }
            }
        },
        shape = TokenShapes.Sm,
        colors =
            TextFieldDefaults.colors(
                focusedContainerColor = Theme.colors.surfaceInput,
                unfocusedContainerColor = Theme.colors.surfaceInput,
                focusedIndicatorColor = Theme.colors.accentPrimary,
                unfocusedIndicatorColor = Theme.colors.outlineGhost,
                focusedTextColor = Theme.colors.textPrimary,
                unfocusedTextColor = Theme.colors.textPrimary,
            ),
        singleLine = true,
    )
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
