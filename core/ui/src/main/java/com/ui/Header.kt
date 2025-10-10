package com.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.theme.ComparaCarrosTheme
import com.theme.TokenColors
import com.theme.TokenDefaultTypography
import com.theme.TokenIconSize

@Composable
fun Header(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit,
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {},
    onSearchFocusChanged: (Boolean) -> Unit = {},
    isSearchFocused: Boolean = false
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
                    tint = TokenColors.Icon,
                    modifier = Modifier.size(TokenIconSize.Medium)
                )
            }

            Text(
                text = "ComparaCarros",
                style = TokenDefaultTypography.titleLarge,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = TokenColors.Primary,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        onSearchFocusChanged(focusState.isFocused)
                    },
            placeholder = {
                Text(
                    text = "Buscar por modelo...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TokenColors.Subtitle
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
                    tint = TokenColors.Subtitle
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
                            tint = TokenColors.Subtitle
                        )
                    }
                }
            },
            shape = RoundedCornerShape(8.dp),
            colors =
                TextFieldDefaults.colors(
                    focusedContainerColor = TokenColors.White,
                    unfocusedContainerColor = TokenColors.White,
                    focusedIndicatorColor = TokenColors.Primary,
                    unfocusedIndicatorColor = TokenColors.Subtitle.copy(alpha = 0.3f)
                ),
            singleLine = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderPreview() {
    ComparaCarrosTheme {
        Header(
            onMenuClick = {},
            searchQuery = "",
            onSearchQueryChange = {},
            onSearchFocusChanged = {},
            isSearchFocused = false
        )
    }
}
