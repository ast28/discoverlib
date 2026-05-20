package com.example.discoverlib.data.remote.api

import com.example.discoverlib.data.remote.dto.AvailabilityResponseDto
import com.example.discoverlib.data.remote.dto.ReservationResponseDto
import com.example.discoverlib.data.remote.dto.ReserveRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HotelApiService {

    @GET("{group_id}/hotels/availability")
    suspend fun getAvailability(
        @Path("group_id") groupId: String,
        @Query("start") start: String,
        @Query("end") end: String,
        @Query("city") city: String
    ): AvailabilityResponseDto


    @POST("{group_id}/reserve")
    suspend fun reserveRoom(
        @Path("group_id") groupId: String,
        @Body request: ReserveRequestDto
    ): ReservationResponseDto


    @POST("{group_id}/cancel")
    suspend fun cancelReservation(
        @Path("group_id") groupId: String,
        @Body request: ReserveRequestDto
    ): ReservationResponseDto

}