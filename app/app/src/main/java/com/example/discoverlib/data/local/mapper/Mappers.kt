package com.example.discoverlib.data.local.mapper

import com.example.discoverlib.data.local.entity.ActivityEntity
import com.example.discoverlib.data.local.entity.TripEntity
import com.example.discoverlib.data.local.entity.UserEntity
import com.example.discoverlib.domain.ActivityCategory
import com.example.discoverlib.domain.Trip
import com.example.discoverlib.domain.TripActivity
import com.example.discoverlib.domain.User
import java.time.LocalDate
import java.time.LocalTime
import kotlin.String

// De dominio a entidad
fun Trip.toEntity(): TripEntity {
    return TripEntity(
        id = id,
        userId = this.userId,
        title = title,
        startDate = startDate.toString(),
        endDate = endDate.toString(),
        description = description,
        price = budgetEur
    )
}

fun TripActivity.toEntity(tripId: String): ActivityEntity {
    return ActivityEntity(
        id = id,
        tripId = tripId,
        title = title,
        description = description,
        location = location,
        date = date.toString(),
        time = time.toString(),
        category = category.name,
        price = costEur,
        photo = photo,
        photo_maps = photo_maps
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        username = username,
        dateOfBirth = dateOfBirth.toString(),
        darkMode = darkMode,
        language = language,
        address = address,
        country = country,
        phoneNumber = phoneNumber,
        acceptReceiveEmails = acceptReceiveEmails
    )
}

// De entidad a dominio
fun TripEntity.toDomain(activitiesList: List<ActivityEntity> = emptyList()): Trip {
    return Trip(
        id = id,
        userId = this.userId,
        title = title,
        startDate = LocalDate.parse(startDate),
        endDate = LocalDate.parse(endDate),
        description = description,
        budgetEur = price,
        activities = activitiesList.map { it.toDomain() }.toMutableList()
    )
}

fun ActivityEntity.toDomain(): TripActivity {
    return TripActivity(
        id = id,
        title = title,
        description = description,
        location = location,
        date = LocalDate.parse(date),
        time = LocalTime.parse(time),
        category = ActivityCategory.valueOf(category),
        costEur = price,
        photo = photo,
        photo_maps = photo_maps
    )
}

fun UserEntity.toDomain(): User {
    return User(
        id = id,
        username = username,
        dateOfBirth = LocalDate.parse(dateOfBirth),
        darkMode = darkMode,
        language = language,
        address = address,
        country = country,
        phoneNumber = phoneNumber,
        acceptReceiveEmails = acceptReceiveEmails
    )
}