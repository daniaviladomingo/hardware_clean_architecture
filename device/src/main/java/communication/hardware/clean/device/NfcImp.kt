package communication.hardware.clean.device

import android.app.Activity
import android.nfc.NfcAdapter
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import communication.hardware.clean.device.exception.IllegalHardwareException
import communication.hardware.clean.domain.nfc.INfc
import io.reactivex.Observable

class NfcImp(
    private val activity: Activity,
    lifecycle: Lifecycle,
    private val flags: Int,
    private val bundle: Bundle
) : INfc, LifecycleObserver {

    init {
        lifecycle.addObserver(this)
    }

    private var rxBus: (ByteArray) -> Unit = {}

    private var nfcAdapter: NfcAdapter = NfcAdapter.getDefaultAdapter(activity)
        ?: throw IllegalHardwareException("Device hasn't NFC feature")

    override fun getIdTag(): Observable<ByteArray> = Observable.create {
        rxBus = { tag ->
            it.onNext(tag)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onStop() {
        nfcAdapter.disableReaderMode(activity)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onStart() {
        nfcAdapter.enableReaderMode(
            activity,
            { tag ->
                rxBus.invoke(tag.id)
            },
            flags,
            bundle
        )
    }
}