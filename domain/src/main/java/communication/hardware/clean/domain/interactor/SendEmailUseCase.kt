package communication.hardware.clean.domain.interactor

import communication.hardware.clean.domain.email.IEmail
import communication.hardware.clean.domain.email.model.Email
import communication.hardware.clean.domain.interactor.type.CompletableUseCaseWithParameter
import io.reactivex.Completable

class SendEmailUseCase(private val email: IEmail) : CompletableUseCaseWithParameter<Email> {
    override fun execute(parameter: Email): Completable = email.sendEmail(parameter)
}