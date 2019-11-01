package communication.hardware.clean.domain.interactor.nfc

import communication.hardware.clean.domain.interactor.type.SingleUseCase
import communication.hardware.clean.domain.nfc.INfc
import io.reactivex.Single

class IsNfcSupportedUseCase(private val nfc: INfc) : SingleUseCase<Boolean> {
    override fun execute(): Single<Boolean> = nfc.isNfcSupported()
}