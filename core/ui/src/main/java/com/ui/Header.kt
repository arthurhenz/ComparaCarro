package com.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.theme.ComparaCarrosTheme
import com.theme.TokenColors
import com.theme.TokenDefaultTypography
import com.theme.TokenIconSize

@Composable
fun Header(modifier: Modifier = Modifier, onMenuClick: () -> Unit) {
    Box(
        modifier = modifier
            .height(56.dp)
            .fillMaxWidth()
    ) {
        IconButton(
            onClick = onMenuClick,
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
}

@Preview(showBackground = true)
@Composable
fun HeaderPreview() {
    ComparaCarrosTheme {
        Header(onMenuClick = {})
    }
}
