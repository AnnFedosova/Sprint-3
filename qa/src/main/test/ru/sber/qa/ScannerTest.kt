package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class ScannerTest {

    @Test
    fun getScanDataScanTimeoutExceptionTest() {
        mockkObject(Random)
        every { Random.nextLong(5000L, 15000L) } returns 15000L
        assertFailsWith<ScanTimeoutException>(
            block = {
                Scanner.getScanData()
            }
        )
    }

    @Test
    fun getScanDataOkTest() {
        mockkObject(Random)
        every { Random.nextLong(5000L, 15000L) } returns 5000L
        val byteArray: ByteArray = Scanner.getScanData()
        assertEquals(100, byteArray.size)
    }

    @AfterEach
    fun afterTests() {
        unmockkAll()
    }
}