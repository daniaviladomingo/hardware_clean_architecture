package communication.hardware.clean.domain.interactor.nfc

import communication.hardware.clean.domain.interactor.type.ObservableUseCase
import communication.hardware.clean.domain.nfc.INfc
import io.reactivex.Observable

class ReadNfcUseCase(private val nfc: INfc) : ObservableUseCase<ByteArray> {
    override fun execute(): Observable<ByteArray> = nfc.getIdTag()
}