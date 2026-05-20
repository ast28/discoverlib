package com.example.discoverlib.data.repository

import com.example.discoverlib.data.remote.api.HotelApiService
import com.example.discoverlib.data.local.mapper.toDomain
import com.example.discoverlib.data.local.mapper.toDto
import com.example.discoverlib.domain.Hotel
import com.example.discoverlib.domain.HotelRepository
import com.example.discoverlib.domain.Reservation
import com.example.discoverlib.domain.ReserveRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HotelRepositoryImpl @Inject constructor(
    private val api: HotelApiService
) : HotelRepository {

    private val groupId = "albasenar"

    override suspend fun getAvailability(start: String, end: String, city: String): List<Hotel> {
        val response = api.getAvailability(groupId, start, end, city)

        return response.available_hotels.map { it.toDomain() }
    }

    override suspend fun reserve(request: ReserveRequest): Reservation {
        val response = api.reserveRoom(groupId, request.toDto())
        return response.reservation?.toDomain() ?: throw Exception(response.message)
    }

    override suspend fun cancel(request: ReserveRequest): String {
        val response = api.cancelReservation(groupId, request.toDto())
        return response.message
    }
}