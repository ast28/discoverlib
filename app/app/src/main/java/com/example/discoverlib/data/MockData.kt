package com.example.discoverlib.data

import com.example.discoverlib.domain.ActivityCategory
import com.example.discoverlib.domain.TeamMember
import com.example.discoverlib.domain.Trip
import com.example.discoverlib.domain.TripActivity

object MockData {
    val featuredTrip = Trip(
        id = "roma-2026",
        city = "Roma",
        period = "23-03-2026 al 28-03-2026",
        nights = 5,
        budgetEur = 257,
        activities = listOf(
            TripActivity(
                id = "colosseum",
                title = "Roma Colosseum Tour",
                description = "Visita guiada al Coliseo y alrededores.",
                location = "P.za del Colosseo, 1, Roma",
                dayLabel = "Lun 23",
                time = "10:00",
                category = ActivityCategory.CULTURE,
                costEur = 10
            ),
            TripActivity(
                id = "lunch",
                title = "Almuerzo en Trastevere",
                description = "Comida local en restaurante con reserva.",
                location = "Trastevere, Roma",
                dayLabel = "Jue 26",
                time = "14:00",
                category = ActivityCategory.FOOD,
                costEur = 20
            ),
            TripActivity(
                id = "villa-borghese",
                title = "Paseo por Villa Borghese",
                description = "Ruta corta por jardines y miradores.",
                location = "Villa Borghese, Roma",
                dayLabel = "Mie 25",
                time = "16:00",
                category = ActivityCategory.NATURE,
                costEur = 5
            )
        )
    )

    val trips = listOf(
        featuredTrip,
        featuredTrip.copy(id = "london-2026", city = "Londres", period = "10-04-2026 al 14-04-2026", budgetEur = 490),
        featuredTrip.copy(id = "paris-2026", city = "París", period = "04-05-2026 al 08-05-2026", budgetEur = 410)
    )

    val galleryTrips = listOf("Londres", "París", "Roma")

    val team = listOf(
        TeamMember("AS", "Alba Senar", "Lead Developer - UI/UX")
    )

    val termsSections = listOf(
        "Aceptacion de terminos" to "Al usar Discoverlib aceptas estas condiciones.",
        "Privacidad y datos" to "Solo recopilamos datos necesarios para la experiencia local de la app.",
        "Uso de la aplicacion" to "La informacion es orientativa y debe verificarse con proveedores oficiales.",
        "Servicios de terceros" to "La app puede integrar servicios externos sujetos a sus propios terminos.",
        "Modificaciones" to "Los terminos pueden actualizarse en futuras versiones de la aplicacion."
    )
}
