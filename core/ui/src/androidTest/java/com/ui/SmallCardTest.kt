package com.ui

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.theme.ComparaCarrosTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SmallCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun smallCard_displaysTitle() {
        composeTestRule.setContent {
            ComparaCarrosTheme {
                SmallCard(
                    image = painterResource(id = R.drawable.ic_launcher_background),
                    onClick = {},
                    title = "Honda Civic 2020",
                    fipe = "R$120.000,00"
                )
            }
        }

        composeTestRule.onNodeWithText("Honda Civic 2020").assertIsDisplayed()
    }

    @Test
    fun smallCard_displaysFipeValue() {
        composeTestRule.setContent {
            ComparaCarrosTheme {
                SmallCard(
                    image = painterResource(id = R.drawable.ic_launcher_background),
                    onClick = {},
                    title = "Honda Civic 2020",
                    fipe = "R$120.000,00"
                )
            }
        }

        composeTestRule.onNodeWithText("R$120.000,00").assertIsDisplayed()
    }

    @Test
    fun smallCard_clickTriggersCallback() {
        var clicked = false
        composeTestRule.setContent {
            ComparaCarrosTheme {
                SmallCard(
                    image = painterResource(id = R.drawable.ic_launcher_background),
                    onClick = { clicked = true },
                    contentDescription = "card-civic",
                    title = "Honda Civic 2020",
                    fipe = "R$120.000,00"
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("card-civic").performClick()
        assertTrue(clicked)
    }

    @Test
    fun smallCardWithSelect_displaysTitle() {
        composeTestRule.setContent {
            ComparaCarrosTheme {
                SmallCard(
                    image = painterResource(id = R.drawable.ic_launcher_background),
                    selected = false,
                    onSelect = {},
                    onClick = {},
                    title = "Saveiro 2021",
                    fipe = "R$30.000,00"
                )
            }
        }

        composeTestRule.onNodeWithText("Saveiro 2021").assertIsDisplayed()
        composeTestRule.onNodeWithText("R$30.000,00").assertIsDisplayed()
    }

    @Test
    fun smallCardWithSelect_clickTriggersCallback() {
        var clicked = false
        composeTestRule.setContent {
            ComparaCarrosTheme {
                SmallCard(
                    image = painterResource(id = R.drawable.ic_launcher_background),
                    selected = false,
                    onSelect = {},
                    onClick = { clicked = true },
                    contentDescription = "card-saveiro",
                    title = "Saveiro 2021",
                    fipe = "R$30.000,00"
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("card-saveiro").performClick()
        assertTrue(clicked)
    }

    @Test
    fun smallCardWithSelect_togglesSelection() {
        var selectedState = false
        composeTestRule.setContent {
            ComparaCarrosTheme {
                SmallCard(
                    image = painterResource(id = R.drawable.ic_launcher_background),
                    selected = selectedState,
                    onSelect = { selectedState = it },
                    onClick = {},
                    title = "Saveiro 2021",
                    fipe = "R$30.000,00"
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Comparar").performClick()
        assertEquals(true, selectedState)
    }

    @Test
    fun smallCardWithSelect_showsComparandoWhenSelected() {
        composeTestRule.setContent {
            ComparaCarrosTheme {
                SmallCard(
                    image = painterResource(id = R.drawable.ic_launcher_background),
                    selected = true,
                    onSelect = {},
                    onClick = {},
                    title = "Saveiro 2021",
                    fipe = "R$30.000,00"
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Comparando").assertIsDisplayed()
    }
}
