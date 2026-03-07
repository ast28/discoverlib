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
    val activities: List<TripActivity>
) {
    fun spentBudget(): Int = activities.sumOf { it.costEur }

    fun remainingBudget(): Int = budgetEur - spentBudget()
}

data class TeamMember(
    val initials: String,
    val name: String,
    val role: String
)
