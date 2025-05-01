package com.marcelo.souza.buscar.cep.presentation.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.marcelo.souza.buscar.cep.domain.model.CepViewData
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailsCepScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val cepData = CepViewData(
        cep = "12213350",
        street = "Rua José Bonifácio de Arantes",
        neighborhood = "Vila Paiva",
        city = "São José dos Campos",
        state = "SP",
        error = false
    )

    private fun setContent() = composeTestRule.setContent {
        DetailsCepScreen(
            navController = rememberNavController(),
            cepData = cepData
        )
    }

    @Test
    fun checkTopBarTitleIsCorrectAndAreDisplayed() {
        setContent()

        composeTestRule
            .onNodeWithTag("topAppBarComponent")
            .assertIsDisplayed()

        composeTestRule.onNode(
            hasText("Detalhes do Cep") and hasParent(hasTestTag("topAppBarComponent")),
            useUnmergedTree = true
        ).assertIsDisplayed()
    }

    @Test
    fun detailsCepScreen_displaysAllFieldsAndBackNavigatesUp() {
        setContent()

        composeTestRule.onNodeWithText(cepData.cep.toString()).assertIsDisplayed()
        composeTestRule.onNodeWithText(cepData.street.toString()).assertIsDisplayed()
        composeTestRule.onNodeWithText(cepData.neighborhood.toString()).assertIsDisplayed()
        composeTestRule.onNodeWithText(cepData.city.toString()).assertIsDisplayed()
        composeTestRule.onNodeWithText(cepData.state.toString()).assertIsDisplayed()
    }
}