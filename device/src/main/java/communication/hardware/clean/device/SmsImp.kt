package communication.hardware.clean.device

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Telephony
import android.telephony.SmsManager
import android.telephony.SmsMessage
import avila.domingo.lifecycle.ILifecycleObserver
import communication.hardware.clean.domain.sms.ISms
import communication.hardware.clean.domain.sms.model.Sms
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class SmsImp(
    private val context: Context
) : ISms, ILifecycleObserver {

    private val smsManager: SmsManager = SmsManager.getDefault()
    private val incomingSmsBroadcastaReceiver = object : BroadcastReceiver() {
        var smsListener: ((Sms) -> Unit)? = null

        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
                getSms(intent)?.run {
                    smsListener?.invoke(this)
                }
            }
        }

        private fun getSms(intent: Intent): Sms? {
            val pdusObj = intent.extras!!.get("pdus") as Array<*>
            for (i in pdusObj.indices) {
                @Suppress("DEPRECATION") val currentMessage =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                        Telephony.Sms.Intents.getMessagesFromIntent(intent)[0]
                    else SmsMessage.createFromPdu(pdusObj[0] as ByteArray)

                return Sms(currentMessage.serviceCenterAddress, currentMessage.displayMessageBody)
            }
            return null
        }
    }

    override fun getSms(): Observable<Sms> = Observable.create {
        incomingSmsBroadcastaReceiver.smsListener = { sms ->
            it.onNext(sms)
        }
    }

    override fun sendSms(sms: Sms): Completable = Completable.create {
        if (sms.text.trim().isEmpty() || sms.destinationAddress.trim().isEmpty()) {
            it.onError(Throwable("destinationAddress & text can't be empty"))
        } else {
            smsManager.sendTextMessage(sms.destinationAddress, null, sms.text, null, null)
            it.onComplete()
        }
    }

    override fun isSupported(): Single<Boolean> = Single.create {
        it.onSuccess(context.packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY))
    }

    override fun resume() {
        context.registerReceiver(
            incomingSmsBroadcastaReceiver,
            IntentFilter().apply { addAction("android.provider.Telephony.SMS_RECEIVED") })
    }

    override fun pause() {
        context.unregisterReceiver(incomingSmsBroadcastaReceiver)
    }
}