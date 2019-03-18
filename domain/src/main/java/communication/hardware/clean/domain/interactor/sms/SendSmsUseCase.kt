package communication.hardware.clean.domain.interactor.sms

import communication.hardware.clean.domain.interactor.type.CompletableUseCaseWithParameter
import communication.hardware.clean.domain.sms.ISms
import communication.hardware.clean.domain.sms.model.Sms
import io.reactivex.Completable

class SendSmsUseCase(private val sms: ISms) : CompletableUseCaseWithParameter<Sms> {
    override fun execute(parameter: Sms): Completable = sms.sendSms(parameter)
}