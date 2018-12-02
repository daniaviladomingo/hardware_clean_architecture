package communication.hardware.clean.device.sms

import android.content.Context
import android.content.IntentFilter
import communication.hardware.clean.domain.sms.ISms
import communication.hardware.clean.domain.sms.model.Sms
import io.reactivex.Completable
import io.reactivex.Single

class SmsImp(
    private val context: Context,
    private val incomingSmsBroadcastaReceiver: IncomingSmsBroadcastaReceiver
) : ISms {
    override fun getSms(): Single<Sms> = Single.create {
        context.registerReceiver(
            incomingSmsBroadcastaReceiver,
            IntentFilter().apply { addAction("android.provider.Telephony.SMS_RECEIVED") })

        incomingSmsBroadcastaReceiver.smsListener = { sms ->
            context.unregisterReceiver(incomingSmsBroadcastaReceiver)
            it.onSuccess(sms)

        }
    }

    override fun sendSms(sms: Sms): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}