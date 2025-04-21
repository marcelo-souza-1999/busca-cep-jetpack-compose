package com.marcelo.souza.buscar.cep.presentation.ui.navigation

sealed class Routes(val route: String) {
    data object SearchCep : Routes("searchCep")
    data object DetailsCep : Routes("detailsCep")
}
