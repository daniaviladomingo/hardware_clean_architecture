package communication.hardware.clean.domain.nfc

import io.reactivex.Observable
import io.reactivex.Single


interface INfc {
    fun getIdTag(): Observable<ByteArray>
    fun isNfcSupported(): Single<Boolean>
}