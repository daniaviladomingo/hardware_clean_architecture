package communication.hardware.clean.device

import android.app.Activity
import android.nfc.NfcAdapter
import android.os.Bundle
import avila.domingo.lifecycle.ILifecycleObserver
import communication.hardware.clean.device.exception.IllegalHardwareException
import communication.hardware.clean.domain.nfc.INfc
import io.reactivex.Observable

class NfcImp(
    private val activity: Activity,
    private val flags: Int,
    private val bundle: Bundle
) : INfc, ILifecycleObserver {

    private var rxBus: (ByteArray) -> Unit = {}

    private var nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(activity)

//    init {
//        nfcAdapter ?: throw IllegalHardwareException("Device hasn't NFC feature")
//    }

    override fun getIdTag(): Observable<ByteArray> =
        Observable.create {
            rxBus = { tag ->
                it.onNext(tag)
            }
        }

    override fun resume() {
        nfcAdapter?.enableReaderMode(
            activity,
            { tag ->
                rxBus.invoke(tag.id)
            },
            flags,
            bundle
        )
    }

    override fun pause() {
        nfcAdapter?.disableReaderMode(activity)
    }
}