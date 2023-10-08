package ru.sber.qa

import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId
import kotlin.test.assertFailsWith


internal class HrDepartmentTest {

    companion object {
        private val SUNDAY_LOCAL_DATE = LocalDate.of(2023, 10, 8)
        private val MONDAY_LOCAL_DATE = LocalDate.of(2023, 10, 9)
        private val TUESDAY_LOCAL_DATE = LocalDate.of(2023, 10, 10)
    }

    private var sut: HrDepartment = HrDepartment

    @MockK
    lateinit var certificateRequestMock: CertificateRequest

    @BeforeEach
    fun init() {
        MockKAnnotations.init(this)
    }

    @Test
    fun receiveRequestWeekendDayExceptionTest() {
        sut.clock = getSundayClock()
        assertFailsWith<WeekendDayException>(
            block = {
                sut.receiveRequest(certificateRequestMock)
            }
        )
    }

    @Test
    fun receiveRequestNotAllowExceptionLabourBookTest() {
        sut.clock = getMondayClock()
        every { certificateRequestMock.employeeNumber } returns 1L
        every { certificateRequestMock.certificateType } returns CertificateType.LABOUR_BOOK

        assertFailsWith<NotAllowReceiveRequestException>(
            block = {
                sut.receiveRequest(certificateRequestMock)
            }
        )
    }

    @Test
    fun receiveRequestNotAllowExceptionNDFLTest() {
        sut.clock = getTuesdayClock()
        every { certificateRequestMock.employeeNumber } returns 1L
        every { certificateRequestMock.certificateType } returns CertificateType.NDFL

        assertFailsWith<NotAllowReceiveRequestException>(
            block = {
                sut.receiveRequest(certificateRequestMock)
            }
        )
    }

    @Test
    fun processNextRequestOkTest() {
        sut.clock = getMondayClock()
        every { certificateRequestMock.employeeNumber } returns 1L
        every { certificateRequestMock.certificateType } returns CertificateType.NDFL
        every { certificateRequestMock.process(1L) } returns mockk()

        sut.receiveRequest(certificateRequestMock)
        sut.processNextRequest(1L)

        verify { certificateRequestMock.process(1L) }
    }

    private fun getSundayClock(): Clock {
        return Clock.fixed(SUNDAY_LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault())
    }

    private fun getMondayClock(): Clock {
        return Clock.fixed(MONDAY_LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault())
    }

    private fun getTuesdayClock(): Clock {
        return Clock.fixed(TUESDAY_LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault())
    }

    @AfterEach
    fun afterTests() {
        unmockkAll()
    }

}