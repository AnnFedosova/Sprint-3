package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class CertificateRequestTest {

    private var sut: CertificateRequest = CertificateRequest(1L, CertificateType.NDFL)

    @Test
    fun processTest() {
        mockkObject(Scanner)
        val scannedData = Random.nextBytes(5)
        every { Scanner.getScanData() } returns scannedData

        val certificate: Certificate = sut.process(2L)

        assertEquals(sut, certificate.certificateRequest)
        assertEquals(2L, certificate.processedBy)
        assertEquals(scannedData, certificate.data)
        verify { Scanner.getScanData() }
    }

    @AfterEach
    fun afterTests() {
        unmockkAll()
    }
}