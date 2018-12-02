package communication.hardware.clean.device.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import android.telephony.SmsMessage
import communication.hardware.clean.domain.sms.model.Sms

class IncomingSmsBroadcastaReceiver : BroadcastReceiver() {

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