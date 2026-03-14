package com.example.discoverlib.domain

interface TripRepository {
    fun getTrips(): List<Trip>
    fun getOneTrip(tripId: String): Trip?
    fun addTrip(trip: Trip): Boolean
    fun editTrip(trip: Trip): Boolean
    fun deleteTrip(tripId: String): Boolean

    fun getActivities(trip: Trip): List<TripActivity>
    fun getOneActivity(tripId: String, activityId: String): TripActivity?
    fun addActivity(tripId: String, activity: TripActivity): Boolean
    fun editActivity(tripId: String, activity: TripActivity): Boolean
    fun deleteActivity(tripId: String, activityId: String): Boolean

    fun getUser(): User?
    fun saveUser(user: User): Boolean
}