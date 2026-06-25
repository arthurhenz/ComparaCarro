package com.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import com.theme.Theme
import com.theme.TokenShapes
import com.theme.TokenSpacing

/**
 * Reusable search input. Self-manages focus: when [isSearchFocused] turns true the field
 * requests focus so the keyboard opens. Used by both the home [Header] and the comparison
 * selection screen.
 */
@Composable
fun SearchField(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchFocusChanged: (Boolean) -> Unit,
    isSearchFocused: Boolean,
    modifier: Modifier =
        Modifier
            .fillMaxWidth()
            .padding(
                start = TokenSpacing.Block,
                end = TokenSpacing.Block,
                top = TokenSpacing.Item,
                bottom = TokenSpacing.Block,
            ),
    placeholder: String = "Buscar por modelo...",
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(isSearchFocused) {
        if (isSearchFocused) focusRequester.requestFocus()
    }

    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        modifier =
            modifier
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    onSearchFocusChanged(focusState.isFocused)
                },
        placeholder = {
            Text(
                text = placeholder,
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
