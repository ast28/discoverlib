package com.example.discoverlib.domain

import java.time.LocalDate

data class Hotel(
    val id: String,
    val name: String,
    val city: String,
    val photo: String?,
    val rooms: List<Room>
)

data class Room(
    val id: String,
    val name: String,
    val price: Double,
    val photo: String?
)

data class Reservation(
    val id: String,
    val hotelId: String,
    val roomId: String,
    val startDate: LocalDate,
    val endDate: LocalDate
)

data class ReserveRequest(
    val hotelId: String,
    val roomId: String,
    val startDate: LocalDate,
    val endDate: LocalDate
)