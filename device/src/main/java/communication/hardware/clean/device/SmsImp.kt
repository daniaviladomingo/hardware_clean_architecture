package communication.hardware.clean.device

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.provider.Telephony
import android.telephony.SmsManager
import android.telephony.SmsMessage
import avila.domingo.lifecycle.ILifecycleObserver
import communication.hardware.clean.device.util.isPermissionGranted
import communication.hardware.clean.domain.sms.ISms
import communication.hardware.clean.domain.sms.model.Sms
import io.reactivex.Completable
import io.reactivex.Single

class SmsImp(
    private val context: Context
) : ISms, ILifecycleObserver {
    private var startListenIncomingSms = false

    private val smsManager: SmsManager = SmsManager.getDefault()
    private val incomingSmsBroadcastaReceiver = IncomingSmsBroadcastaReceiver()

    @Synchronized
    override fun resume() {
        if (startListenIncomingSms) {
            context.registerReceiver(
                incomingSmsBroadcastaReceiver,
                IntentFilter().apply { addAction("android.provider.Telephony.SMS_RECEIVED") })
        }
    }

    @Synchronized
    override fun pause() {
        if (startListenIncomingSms) {
            context.unregisterReceiver(incomingSmsBroadcastaReceiver)
        }
    }

    override fun getSms(): Single<Sms> = Single.create {
        if (!context.isPermissionGranted(Manifest.permission.RECEIVE_SMS)) {
            throw IllegalAccessError("${Manifest.permission.RECEIVE_SMS} do not granted")
        }
        startListenIncomingSms = true
        resume()
        incomingSmsBroadcastaReceiver.smsListener = { sms ->
            it.onSuccess(sms)
        }
    }

    override fun sendSms(sms: Sms): Completable = Completable.create {
//        if (!context.isPermissionGranted(Manifest.permission.SEND_SMS)) {
//            throw IllegalAccessError("${Manifest.permission.SEND_SMS} do not granted")
//        }

        if (sms.text.trim().isEmpty() || sms.destinationAddress.trim().isEmpty()) {
            it.onError(Throwable("destinationAddress & text can't be empty"))
        } else {
            smsManager.sendTextMessage(sms.destinationAddress, null, sms.text, null, null)
            it.onComplete()
        }
    }

    private inner class IncomingSmsBroadcastaReceiver : BroadcastReceiver() {

        lateinit var smsListener: ((Sms) -> Unit)

        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
                getSms(intent)?.run {
                    smsListener.invoke(this)
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
}