package communication.hardware.clean.domain.interactor.flash

import communication.hardware.clean.domain.flash.IFlash
import communication.hardware.clean.domain.flash.model.FlashMode
import communication.hardware.clean.domain.interactor.type.SingleUseCase
import io.reactivex.Single

class GetFlashModeUseCase(private val flash: IFlash) : SingleUseCase<FlashMode> {
    override fun execute(): Single<FlashMode> = flash.mode()

}