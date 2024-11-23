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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.marcelo.souza.buscar.cep.R
import com.marcelo.souza.buscar.cep.presentation.theme.CEPTheme
import com.marcelo.souza.buscar.cep.presentation.ui.navigation.Routes
import com.marcelo.souza.buscar.cep.presentation.ui.screens.SearchCepScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CepApp()
        }
    }
}

@Composable
fun CepApp() {
    CEPTheme {
        SetupNavigation()
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
    }
}