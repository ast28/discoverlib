package com.example.discoverlib.data.remote.dto

data class ReserveRequestDto(
    val hotelId: String,
    val roomId: String,
    val startDate: String,
    val endDate: String
)