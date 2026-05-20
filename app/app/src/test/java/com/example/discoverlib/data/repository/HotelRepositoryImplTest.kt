package com.example.discoverlib.data.repository

import com.example.discoverlib.data.remote.api.HotelApiService
import com.example.discoverlib.data.remote.dto.AvailabilityResponseDto
import com.example.discoverlib.data.remote.dto.HotelDto
import com.example.discoverlib.data.remote.dto.ReservationDto
import com.example.discoverlib.data.remote.dto.ReservationResponseDto
import com.example.discoverlib.data.remote.dto.RoomDto
import com.example.discoverlib.domain.ReserveRequest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.LocalDate

class HotelRepositoryImplTest {

    private lateinit var mockApi: HotelApiService

    private lateinit var repository: HotelRepositoryImpl

    @Before
    fun setUp() {
        mockApi = mock()
        repository = HotelRepositoryImpl(mockApi)
    }

    @Test
    fun `hotelListMapped`() = runTest {
        val mockRoomDto = RoomDto(id = "r1", name = "Doble", price = 120.0, photo = null)
        val mockHotelDto = HotelDto(id = "h1", name = "Hotel Paris", city = "Paris", photo = null, rooms = listOf(mockRoomDto))
        val mockResponse = AvailabilityResponseDto(listOf(mockHotelDto))

        whenever(mockApi.getAvailability(any(), any(), any(), any())).thenReturn(mockResponse)

        val result = repository.getAvailability("2026-06-01", "2026-06-10", "Paris")

        assertEquals(1, result.size)
        assertEquals("Hotel Paris", result[0].name)
        assertEquals("Doble", result[0].rooms[0].name)
    }

    @Test
    fun `reserveMapped`() = runTest {
        val mockReservationDto = ReservationDto(id = "res1", hotelId = "h1", roomId = "r1", startDate = "2026-06-01", endDate = "2026-06-10")
        val mockResponse = ReservationResponseDto(message = "Success", reservation = mockReservationDto)

        whenever(mockApi.reserveRoom(any(), any())).thenReturn(mockResponse)

        val request = ReserveRequest("h1", "r1", LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 10))

        val result = repository.reserve(request)

        assertEquals("res1", result.id)
        assertEquals("h1", result.hotelId)
        assertEquals(LocalDate.of(2026, 6, 1), result.startDate)
    }

    @Test
    fun `cancelDone`() = runTest {
        val mockResponse = ReservationResponseDto(message = "Reservation cancelled", reservation = null)
        whenever(mockApi.cancelReservation(any(), any())).thenReturn(mockResponse)

        val request = ReserveRequest("h1", "r1", LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 10))

        val resultMessage = repository.cancel(request)

        assertEquals("Reservation cancelled", resultMessage)
    }
}