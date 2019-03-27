package communication.hardware.clean.domain.nfc

import io.reactivex.Observable


interface INfc {
    fun getIdTag(): Observable<ByteArray>
}