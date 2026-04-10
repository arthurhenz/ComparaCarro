package com.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.tools.screenshot.PreviewTest
import com.theme.ComparaCarrosTheme

@PreviewTest
@Preview(showBackground = true)
@Composable
fun SmallCardSelectedScreenshot() {
    ComparaCarrosTheme {
        SmallCard(
            image = painterResource(id = R.drawable.ic_launcher_background),
            selected = true,
            onSelect = {},
            onClick = {},
            title = "Saveiro Pega no Breu",
            fipe = "R$30.000,00"
        )
    }
}

@PreviewTest
@Preview(showBackground = true)
@Composable
fun SmallCardUnselectedScreenshot() {
    ComparaCarrosTheme {
        SmallCard(
            image = painterResource(id = R.drawable.ic_launcher_background),
            selected = false,
            onSelect = {},
            onClick = {},
            title = "Audi A4 Sedan 2019",
            fipe = "R$150.000,00"
        )
    }
}

@PreviewTest
@Preview(showBackground = true)
@Composable
fun SmallCardSimpleScreenshot() {
    ComparaCarrosTheme {
        SmallCard(
            image = painterResource(id = R.drawable.ic_launcher_background),
            onClick = {},
            title = "Honda Civic 2020",
            fipe = "R$120.000,00"
        )
    }
}

@PreviewTest
@Preview(showBackground = true)
@Composable
fun SmallCardLongTextScreenshot() {
    ComparaCarrosTheme {
        SmallCard(
            image = painterResource(id = R.drawable.ic_launcher_background),
            selected = true,
            onSelect = {},
            onClick = {},
            title = "Saveiro Pega no Breu Audi A4 Sedan 2019",
            fipe = "R$30.000,00 - Valor médio da tabela FIPE para este modelo"
        )
    }
}

@PreviewTest
@Preview(showBackground = true)
@Composable
fun SmallCardRowScreenshot() {
    ComparaCarrosTheme {
        Row {
            SmallCard(
                image = painterResource(id = R.drawable.ic_launcher_background),
                selected = true,
                onSelect = {},
                onClick = {},
                title = "Saveiro 2021",
                fipe = "R$30.000,00"
            )
            Spacer(modifier = Modifier.width(8.dp))
            SmallCard(
                image = painterResource(id = R.drawable.ic_launcher_background),
                selected = false,
                onSelect = {},
                onClick = {},
                title = "Civic 2020",
                fipe = "R$120.000,00"
            )
        }
    }
}
