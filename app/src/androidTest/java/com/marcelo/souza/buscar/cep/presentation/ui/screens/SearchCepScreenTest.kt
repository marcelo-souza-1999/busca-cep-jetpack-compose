package com.marcelo.souza.buscar.cep.presentation.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.marcelo.souza.buscar.cep.domain.model.CepViewData
import com.marcelo.souza.buscar.cep.domain.usecase.GetCepUseCase
import com.marcelo.souza.buscar.cep.presentation.ui.navigation.Routes
import com.marcelo.souza.buscar.cep.presentation.ui.utils.Constants
import com.marcelo.souza.buscar.cep.presentation.viewmodel.CepViewModel
import com.marcelo.souza.buscar.cep.presentation.viewmodel.viewstate.ErrorType.EmptyCep
import com.marcelo.souza.buscar.cep.presentation.viewmodel.viewstate.ErrorType.InvalidCep
import com.marcelo.souza.buscar.cep.presentation.viewmodel.viewstate.ErrorType.NetworkError
import com.marcelo.souza.buscar.cep.presentation.viewmodel.viewstate.State
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4::class)
class SearchCepScreenTest : KoinTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewStateFlow: MutableStateFlow<State<CepViewData>>
    private lateinit var viewModel: CepViewModel

    private val getCepUseCase = mockk<GetCepUseCase>(relaxed = true)

    @Before
    fun setup() {
        viewStateFlow = MutableStateFlow(State.Initial)

        viewModel = CepViewModel(getCepUseCase).apply {
            viewState = viewStateFlow
        }

        if (GlobalContext.getOrNull() == null) {
            startKoin {
                modules(module {
                    viewModel { viewModel }
                })
            }
        }
    }

    private fun setContent() = composeTestRule.setContent {
        SearchCepScreen(
            navController = rememberNavController(),
            viewModel = viewModel
        )
    }

    @Test
    fun checkTopBarTitleIsCorrectAndAreDisplayed() {
        setContent()

        composeTestRule
            .onNodeWithTag("topAppBarComponent")
            .assertIsDisplayed()

        composeTestRule.onNode(
            hasText("Cep") and hasParent(hasTestTag("topAppBarComponent")),
            useUnmergedTree = true
        ).assertIsDisplayed()
    }

    @Test
    fun checkAllElementsAreDisplayed() {
        setContent()
        composeTestRule.onNode(
            hasTestTag("fieldCep") and hasText("Cep")
        ).assertIsDisplayed()
        composeTestRule.onNodeWithText("Rua").assertIsDisplayed()
        composeTestRule.onNodeWithText("Bairro").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cidade").assertIsDisplayed()
        composeTestRule.onNodeWithText("Estado").assertIsDisplayed()
        composeTestRule.onNode(
            hasTestTag("primaryButton") and hasText("Buscar Cep")
        ).assertIsDisplayed()
        composeTestRule.onNodeWithText("Continuar").assertIsDisplayed()
    }

    @Test
    fun checkALlDialogCepIsEmptyAreDisplayed() {
        viewStateFlow.value = State.Error(EmptyCep)

        setContent()

        composeTestRule.onNodeWithTag("primaryButton").performClick()

        composeTestRule.onNodeWithText("Informe o CEP para continuar.").assertIsDisplayed()
    }

    @Test
    fun primaryButton_withInvalidCep_showsInvalidCepErrorDialog() {
        viewStateFlow.value = State.Error(InvalidCep)

        setContent()

        composeTestRule.onNodeWithTag("fieldCep").performTextInput("123")

        composeTestRule.onNodeWithTag("primaryButton").performClick()

        composeTestRule.onNodeWithText("Digite um CEP válido.").assertIsDisplayed()
    }

    @Test
    fun primaryButton_withNetworkError_showsNetworkErrorDialog() {
        val cepData = CepViewData(
            cep = "12213350",
            street = "Rua José Bonifácio de Arantes",
            neighborhood = "Vila Paiva",
            city = "São José dos Campos",
            state = "São Paulo",
            error = false
        )

        viewStateFlow.value = State.Error(NetworkError)

        setContent()

        composeTestRule.onNodeWithTag("primaryButton").performClick()

        composeTestRule.onNodeWithText("Verifique sua internet e tente novamente.")
            .assertIsDisplayed()

        viewStateFlow.value = State.Success(cepData)

        composeTestRule.onNodeWithText("Tentar novamente").performClick()
    }

    @Test
    fun checkALlDialogWarningAreDisplayed() {
        setContent()

        composeTestRule.onNodeWithTag("secondaryButton").performClick()

        composeTestRule
            .onNodeWithText("Para continuar, as informações do CEP precisam ser carregadas com sucesso.")
            .assertIsDisplayed()
    }

    @Test
    fun primaryButton_callsGetDataCep_andDisplaysResults() {
        val cepData = CepViewData(
            cep = "12213350",
            street = "Rua José Bonifácio de Arantes",
            neighborhood = "Vila Paiva",
            city = "São José dos Campos",
            state = "São Paulo",
            error = false
        )

        coEvery { getCepUseCase(cepData.cep.toString()) } returns flow {
            emit(State.Success(cepData))
        }

        setContent()

        composeTestRule
            .onNodeWithTag("fieldCep")
            .performTextInput(cepData.cep.toString())

        composeTestRule
            .onNodeWithTag("primaryButton")
            .performClick()

        composeTestRule.waitUntil(timeoutMillis = 5_000) {
            composeTestRule
                .onAllNodesWithText(cepData.street.toString())
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule.onNodeWithText(cepData.street.toString()).assertIsDisplayed()
        composeTestRule.onNodeWithText(cepData.neighborhood.toString()).assertIsDisplayed()
        composeTestRule.onNodeWithText(cepData.city.toString()).assertIsDisplayed()
        composeTestRule.onNodeWithText(cepData.state.toString()).assertIsDisplayed()
    }

    @Test
    fun secondaryButton_withSuccessState_navigatesToDetailsCepWithAllArgs() {
        val cepData = CepViewData(
            cep = "12213350",
            street = "Rua José Bonifácio de Arantes",
            neighborhood = "Vila Paiva",
            city = "São José dos Campos",
            state = "São Paulo",
            error = false
        )
        viewStateFlow.value = State.Success(cepData)

        var navController: NavController? = null

        var navigatedRoute: String? = null

        composeTestRule.setContent {
            navController = rememberNavController().apply {
                addOnDestinationChangedListener { _, destination, _ ->
                    navigatedRoute = destination.route
                }
            }
            NavHost(
                navController = navController,
                startDestination = Routes.SearchCep.route
            ) {
                composable(Routes.SearchCep.route) {
                    SearchCepScreen(
                        navController = navController,
                        viewModel = viewModel
                    )
                }
                composable(
                    route = Routes.DetailsCep.route +
                            "/{${Constants.CEP}}" +
                            "/{${Constants.STREET}}" +
                            "/{${Constants.NEIGHBORHOOD}}" +
                            "/{${Constants.CITY}}" +
                            "/{${Constants.STATE}}",
                    arguments = listOf(
                        navArgument(Constants.CEP) { type = NavType.StringType },
                        navArgument(Constants.STREET) { type = NavType.StringType },
                        navArgument(Constants.NEIGHBORHOOD) { type = NavType.StringType },
                        navArgument(Constants.CITY) { type = NavType.StringType },
                        navArgument(Constants.STATE) { type = NavType.StringType },
                    )
                ) {}
            }
        }

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("secondaryButton")
            .performClick()

        assertTrue(
            "Deveria ter navegado para DetailsCep",
            navigatedRoute?.startsWith("detailsCep/") == true
        )

        composeTestRule.runOnIdle {
            val entry = navController
                ?.currentBackStackEntry
                ?: error("Não navegou para DetailsCep")

            val args = entry.arguments

            assertEquals(cepData.cep, args?.getString(Constants.CEP))
            assertEquals(cepData.street, args?.getString(Constants.STREET))
            assertEquals(cepData.neighborhood, args?.getString(Constants.NEIGHBORHOOD))
            assertEquals(cepData.city, args?.getString(Constants.CITY))
            assertEquals(cepData.state, args?.getString(Constants.STATE))
        }
    }

    @Test
    fun secondaryButton_withNonSuccessState_showsWarningDialog() {
        viewStateFlow.value = State.Initial

        setContent()

        composeTestRule
            .onNodeWithTag("secondaryButton")
            .performClick()

        composeTestRule.onNodeWithText(
            "Para continuar, as informações do CEP precisam ser carregadas com sucesso."
        ).assertIsDisplayed()
    }
}