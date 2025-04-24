package com.marcelo.souza.buscar.cep.extensions

import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class CheckNetworkTest {

    @Test
    fun `should return true for UnknownHostException`() {
        val exception = UnknownHostException()
        assertTrue(exception.isNetworkError())
    }

    @Test
    fun `should return true for SocketTimeoutException`() {
        val exception = SocketTimeoutException()
        assertTrue(exception.isNetworkError())
    }

    @Test
    fun `should return true when cause is UnknownHostException`() {
        val exception = IOException(UnknownHostException())
        assertTrue(exception.isNetworkError())
    }

    @Test
    fun `should return true when cause is SocketTimeoutException`() {
        val exception = IOException(SocketTimeoutException())
        assertTrue(exception.isNetworkError())
    }

    @Test
    fun `should return false for other exceptions`() {
        val exception = IOException("Some other IO error")
        assertFalse(exception.isNetworkError())
    }
}