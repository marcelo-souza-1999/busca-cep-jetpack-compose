package com.marcelo.souza.buscar.cep.extensions

import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.isNetworkError() =
    this is UnknownHostException || cause is UnknownHostException ||
            this is SocketTimeoutException || cause is SocketTimeoutException
