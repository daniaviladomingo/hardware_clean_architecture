package communication.hardware.clean.domain.email

import communication.hardware.clean.domain.email.model.Email
import io.reactivex.Completable

interface IEmail {
    fun sendEmail(email: Email): Completable
}