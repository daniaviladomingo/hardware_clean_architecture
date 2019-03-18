package communication.hardware.clean.domain.interactor.sms

import communication.hardware.clean.domain.interactor.type.SingleUseCase
import communication.hardware.clean.domain.sms.ISms
import communication.hardware.clean.domain.sms.model.Sms
import io.reactivex.Single

class GetSmsUseCase(private val sms: ISms) : SingleUseCase<Sms> {
    override fun execute(): Single<Sms> = sms.getSms()
}