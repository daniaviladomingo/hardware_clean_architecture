package communication.hardware.clean.domain.sms.model

data class Sms(
    val destinationAddress: String,
    val text: String
)