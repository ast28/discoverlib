package com.example.discoverlib.data.remote.dto

data class HotelDto(
    val id: String?,
    val name: String,
    val city: String,
    val photo: String?,
    val rooms: List<RoomDto> = emptyList()
)