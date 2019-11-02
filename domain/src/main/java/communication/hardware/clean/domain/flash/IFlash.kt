package communication.hardware.clean.domain.flash

import communication.hardware.clean.domain.IHardwareSupported
import communication.hardware.clean.domain.flash.model.FlashMode
import io.reactivex.Completable
import io.reactivex.Single

interface IFlash : IHardwareSupported {
    fun mode(mode: FlashMode): Completable
    fun mode(): Single<FlashMode>
}