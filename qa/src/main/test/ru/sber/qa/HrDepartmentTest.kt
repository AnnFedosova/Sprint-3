package ru.sber.qa

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.lang.reflect.Field
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import java.util.stream.Stream
import kotlin.test.assertEquals


internal class HrDepartmentTest {

    companion object {

        private val SATURDAY_LOCAL_DATE = LocalDate.of(2023, 10, 7)
        private val SUNDAY_LOCAL_DATE = LocalDate.of(2023, 10, 8)
        private val MONDAY_LOCAL_DATE = LocalDate.of(2023, 10, 9)
        private val TUESDAY_LOCAL_DATE = LocalDate.of(2023, 10, 10)
    }

    private var sut: HrDepartment = HrDepartment

    @MockK
    lateinit var certificateRequestMock: CertificateRequest

    @MockK
    lateinit var certificateMock: Certificate

    @BeforeEach
    fun init() {
        MockKAnnotations.init(this)
    }

    @ParameterizedTest
    @MethodSource("weekendDayProvider")
    fun receiveRequestWeekendExceptionTest(testLocalDate: LocalDate) {
        sut.clock = buildClock(testLocalDate)

        assertThrows<WeekendDayException> {
            sut.receiveRequest(certificateRequestMock)
        }
    }

    @ParameterizedTest
    @MethodSource("notAllowReceiveRequestProvider")
    fun receiveRequestNotAllowReceiveRequestExceptionTest(testLocalDate: LocalDate, certificateType: CertificateType) {
        sut.clock = buildClock(testLocalDate)
        every { certificateRequestMock.certificateType } returns certificateType

        assertThrows<NotAllowReceiveRequestException> {
            sut.receiveRequest(certificateRequestMock)
        }
    }

    @Test
    fun receiveRequestOkTest() {
        sut.clock = buildClock(MONDAY_LOCAL_DATE)
        every { certificateRequestMock.certificateType } returns CertificateType.NDFL

        sut.receiveRequest(certificateRequestMock)

        val incomeBox = getSutIncomeBox()
        assertEquals(1, incomeBox.size)
        assertEquals(certificateRequestMock, incomeBox[0])
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun processNextRequestOkTest() {
        val incomeBox = getSutIncomeBox()
        val outcomeBox = getSutOutcomeBox()
        incomeBox.push(certificateRequestMock)
        every { certificateRequestMock.process(any()) } returns certificateMock

        sut.processNextRequest(1L)

        verify { certificateRequestMock.process(1L) }
        assertEquals(1, outcomeBox.size)
        assertEquals(certificateMock, outcomeBox[0])
    }

    @AfterEach
    fun afterTests() {
        unmockkAll()
    }

    private fun buildClock(localDate: LocalDate): Clock {
        return Clock.fixed(
            localDate.atStartOfDay(ZoneId.systemDefault()).toInstant(),
            ZoneId.systemDefault()
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun getSutIncomeBox(): LinkedList<CertificateRequest> {
        val incomeBoxField: Field = HrDepartment::class.java.getDeclaredField("incomeBox")
        incomeBoxField.isAccessible = true
        return incomeBoxField.get(sut) as LinkedList<CertificateRequest>
    }

    @Suppress("UNCHECKED_CAST")
    private fun getSutOutcomeBox(): LinkedList<Certificate> {
        val outcomeBoxField: Field = HrDepartment::class.java.getDeclaredField("outcomeBox")
        outcomeBoxField.isAccessible = true
        return outcomeBoxField.get(sut) as LinkedList<Certificate>
    }
}