package com.example.discoverlib.domain

enum class ActivityCategory {
    CULTURE,
    FOOD,
    NATURE
}

data class TripActivity(
    val id: String,
    val title: String,
    val description: String,
    val location: String,
    val dayLabel: String,
    val time: String,
    val category: ActivityCategory,
    val costEur: Int,
    val photo: Int,
    val photo_mapa: Int
)

data class Trip(
    val id: String,
    val city: String,
    val period: String,
    val nights: Int,
    val budgetEur: Int,
    val activities: MutableList<TripActivity> // Cambiado a MutableList para poder añadir/quitar
) {
    fun spentBudget(): Int = activities.sumOf { it.costEur }

    fun remainingBudget(): Int = budgetEur - spentBudget()

    fun optimizeBudgetDistribution() {
        // @TODO Implement smart budget distribution algorithm
    }

    fun addActivity(activity: TripActivity) {
        // @TODO Implement logic to add a new activity
    }

    fun removeActivity(activityId: String) {
        // @TODO Implement logic to remove an activity
    }

    fun addPhoto(photo: Image) {
        // @TODO Implement logic to add a photo to the trip
    }

    fun deletePhoto(photoId: String) {
        // @TODO Implement logic to delete a photo from the trip
    }
}

data class TeamMember(
    val initials: String,
    val name: String,
    val role: String
)


data class Image(
    val id: String,
    val imageUrl: String
)

data class User(
    val id: String,
    val email: String,
    var displayName: String
) {
    fun addNewTrip(trip: Trip) {
        // @TODO Implement logic to add a new trip for the user
    }

    fun deleteTrip(tripId: String) {
        // @TODO Implement logic to delete a user's trip
    }

    fun updateProfile(newName: String) {
        // @TODO Implement logic to update user profile
    }
}

data class Preferences(
    val userId: String,
    var notificationsEnabled: Boolean,
    var preferredLanguage: String,
    var isDarkTheme: Boolean
) {
    fun changeLanguage(lang: String) {
        // @TODO Logic to change app language
    }

    fun changeTheme(isDark: Boolean) {
        // @TODO Logic to toggle dark/light theme
    }

    fun activateNotifications(active: Boolean) {
        // @TODO Logic to enable/disable notifications
    }

    fun updatePreferences() {
        // @TODO Logic to save preferences
    }
}

class Authentication {
    fun signUp(email: String, password: String) {
        // @TODO Logic to register a new user
    }

    fun login(email: String, password: String) {
        // @TODO Logic to authenticate user
    }

    fun logout() {
        // @TODO Logic to log out user
    }

    fun resetPassword(email: String) {
        // @TODO Logic to reset password
    }
}