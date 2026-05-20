package com.example.discoverlib.data.remote.dto

data class ReservationDto(
    val id: String,
    val hotelId: String,
    val roomId: String,
    val startDate: String,
    val endDate: String
)