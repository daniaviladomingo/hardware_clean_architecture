package communication.hardware.clean.domain.location.model

data class Location(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float
)