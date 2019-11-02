package communication.hardware.clean.domain.nfc

import communication.hardware.clean.domain.IHardwareSupported
import io.reactivex.Observable

interface INfc : IHardwareSupported {
    fun getIdTag(): Observable<ByteArray>
}