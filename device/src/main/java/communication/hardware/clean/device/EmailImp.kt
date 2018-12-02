package communication.hardware.clean.device

import communication.hardware.clean.domain.email.IEmail
import communication.hardware.clean.domain.email.model.Email
import io.reactivex.Completable
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

class EmailImp(
    private val properties: Properties,
    private val password: String
) : IEmail {
    override fun sendEmail(email: Email): Completable = Completable.create {
        val session = Session.getInstance(
            properties,
            object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication =
                    PasswordAuthentication(email.from, password)
            }
        )

        Transport.send(
            MimeMessage(session).apply {
                setFrom(InternetAddress(email.from))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.to))
                subject = email.subject

                setContent(
                    MimeMultipart().apply {
                        addBodyPart(
                            MimeBodyPart().apply {
                                setText(email.body)
                            }
                        )
                    }
                )
            }
        )
        it.onComplete()
    }
}