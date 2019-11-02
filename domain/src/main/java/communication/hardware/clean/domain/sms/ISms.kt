package communication.hardware.clean.domain.sms

import communication.hardware.clean.domain.IHardwareSupported
import communication.hardware.clean.domain.sms.model.Sms
import io.reactivex.Completable
import io.reactivex.Single

interface ISms: IHardwareSupported {
    fun getSms(): Single<Sms>
    fun sendSms(sms: Sms): Completable
}