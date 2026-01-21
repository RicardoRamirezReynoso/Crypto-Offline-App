package com.example.cryptooffline

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MarketScreenTest {

    // Inicializa Hilt antes de que se lance la actividad
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    // Lanza la actividad principal de la aplicación
    @get:Rule(order = 1)
    var composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        // Inyecta las dependencias necesarias antes de ejecutar los tests
        hiltRule.inject()
    }

    @Test
    fun changeCurrency_fromUsdToMxn_updatesUiCorrectly() {
        // Se espera a que la red responda y "Bitcoin" aparezca en pantalla
        // Se usa un timeout amplio por si la API tarda en responder
        composeTestRule.waitUntil(timeoutMillis = 60_000L) {
            composeTestRule.onNodeWithText("Bitcoin", substring = true).isDisplayed()
        }

        // Se verifica que el estado inicial muestra el par BTC/USD
        composeTestRule.onNodeWithText("BTC/USD", substring = true).assertIsDisplayed()

        // Se abre el menú de "Opciones"
        composeTestRule.onNodeWithContentDescription("Opciones").performClick()

        composeTestRule.onNodeWithText("Cambiar a MXN").performClick()

        // Se espera que la UI se actualice tras el cambio de moneda
        composeTestRule.waitUntil(timeoutMillis = 10_000L) {
            composeTestRule.onNodeWithText("BTC/MXN", substring = true).isDisplayed()
        }

        // Confirmación final
        composeTestRule.onNodeWithText("BTC/MXN", substring = true).assertIsDisplayed()
    }
}