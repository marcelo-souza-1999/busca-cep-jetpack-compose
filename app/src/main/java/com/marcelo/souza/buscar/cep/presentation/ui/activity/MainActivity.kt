package com.marcelo.souza.buscar.cep.presentation.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.integerResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.marcelo.souza.buscar.cep.R
import com.marcelo.souza.buscar.cep.domain.model.CepViewData
import com.marcelo.souza.buscar.cep.presentation.theme.CEPTheme
import com.marcelo.souza.buscar.cep.presentation.ui.navigation.Routes
import com.marcelo.souza.buscar.cep.presentation.ui.screens.DetailsCepScreen
import com.marcelo.souza.buscar.cep.presentation.ui.screens.SearchCepScreen
import com.marcelo.souza.buscar.cep.presentation.ui.utils.Constants
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.core.annotation.KoinExperimentalAPI

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CepApp()
        }
    }
}

@OptIn(KoinExperimentalAPI::class)
@Composable
fun CepApp() {
    CEPTheme {
        KoinAndroidContext {
            SetupNavigation()
        }
    }
}

@Composable
private fun SetupNavigation() {
    val navController = rememberNavController()
    val duration = integerResource(id = R.integer.duration_animation)

    NavHost(
        navController = navController,
        startDestination = Routes.SearchCep.route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(duration)
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(duration)
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(duration)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(duration)
            )
        }
    ) {
        composable(route = Routes.SearchCep.route) {
            SearchCepScreen(navController)
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
        ) { backStackEntry ->
            val args = backStackEntry.arguments
            val cep = args?.getString(Constants.CEP).orEmpty()
            val street = args?.getString(Constants.STREET).orEmpty()
            val neighborhood = args?.getString(Constants.NEIGHBORHOOD).orEmpty()
            val city = args?.getString(Constants.CITY).orEmpty()
            val state = args?.getString(Constants.STATE).orEmpty()

            DetailsCepScreen(
                navController = navController,
                cepData = CepViewData(
                    cep = cep,
                    street = street,
                    neighborhood = neighborhood,
                    city = city,
                    state = state,
                    error = false
                )
            )
        }
    }
}