package communication.hardware.clean.domain.interactor.sms

import communication.hardware.clean.domain.interactor.type.ObservableUseCase
import communication.hardware.clean.domain.sms.ISms
import communication.hardware.clean.domain.sms.model.Sms
import io.reactivex.Observable

class GetSmsUseCase(private val sms: ISms) : ObservableUseCase<Sms> {
    override fun execute(): Observable<Sms> = sms.getSms()
}