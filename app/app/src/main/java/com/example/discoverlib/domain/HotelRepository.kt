package com.example.discoverlib.domain

interface HotelRepository {
    suspend fun getAvailability(start: String, end: String, city: String): List<Hotel>
    suspend fun reserve(request: ReserveRequest): Reservation
    suspend fun cancel(request: ReserveRequest): String
}