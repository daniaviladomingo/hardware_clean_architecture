package communication.hardware.clean.domain.email.model

data class Email(
    val from: String,
    val to: String,
    val subject: String,
    val body: String
)