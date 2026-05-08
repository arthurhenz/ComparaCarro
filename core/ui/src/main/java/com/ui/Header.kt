package com.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import com.theme.Theme
import com.theme.TokenIconSize
import com.theme.TokenShapes
import com.theme.TokenSpacing

@Composable
fun Header(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit,
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {},
    onSearchFocusChanged: (Boolean) -> Unit = {},
    isSearchFocused: Boolean = false,
    title: String = ""
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier =
            modifier
                .fillMaxWidth()
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable(
                        indication = null,
                        interactionSource = interactionSource
                    ) {
                        if (isSearchFocused) {
                            focusManager.clearFocus()
                        }
                    }
        ) {
            IconButton(
                onClick = {
                    if (isSearchFocused) {
                        focusManager.clearFocus()
                    } else {
                        onMenuClick()
                    }
                },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu",
                    tint = Theme.colors.textPrimary,
                    modifier = Modifier.size(TokenIconSize.Medium)
                )
            }

            Text(
                text = title,
                style = Theme.typography.headlineLarge,
                color = Theme.colors.accentPrimary,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        SearchField(
            searchQuery = searchQuery,
            onSearchQueryChange = onSearchQueryChange,
            onSearchFocusChanged = onSearchFocusChanged,
            isSearchFocused = isSearchFocused,
            focusRequester = focusRequester,
            focusManager = focusManager
        )
    }
}

@Composable
fun Header(
    modifier: Modifier = Modifier,
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {},
    onSearchFocusChanged: (Boolean) -> Unit = {},
    isSearchFocused: Boolean = false
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Column(
        modifier =
            modifier
                .fillMaxWidth()
    ) {
        SearchField(
            searchQuery = searchQuery,
            onSearchQueryChange = onSearchQueryChange,
            onSearchFocusChanged = onSearchFocusChanged,
            isSearchFocused = isSearchFocused,
            focusRequester = focusRequester,
            focusManager = focusManager
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
    focusManager: androidx.compose.ui.focus.FocusManager
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
                    bottom = TokenSpacing.Block
                )
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    onSearchFocusChanged(focusState.isFocused)
                },
        placeholder = {
            Text(
                text = "Buscar por modelo...",
                style = Theme.typography.bodyMedium,
                color = Theme.colors.textSecondary
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                tint = Theme.colors.textSecondary
            )
        },
        trailingIcon = {
            if (isSearchFocused) {
                IconButton(
                    onClick = {
                        onSearchQueryChange("")
                        focusManager.clearFocus()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Clear and unfocus",
                        tint = Theme.colors.textSecondary
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
                unfocusedTextColor = Theme.colors.textPrimary
            ),
        singleLine = true
    )
}

@Preview(showBackground = true)
@Composable
fun HeaderPreview() {
    Theme {
        Header(
            onMenuClick = {},
            searchQuery = "",
            onSearchQueryChange = {},
            onSearchFocusChanged = {},
            isSearchFocused = false
        )
    }
}
