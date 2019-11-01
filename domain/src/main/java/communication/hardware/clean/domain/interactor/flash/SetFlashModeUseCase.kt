package communication.hardware.clean.domain.interactor.flash

import communication.hardware.clean.domain.flash.IFlash
import communication.hardware.clean.domain.flash.model.FlashMode
import communication.hardware.clean.domain.interactor.type.CompletableUseCaseWithParameter
import io.reactivex.Completable

class SetFlashModeUseCase(private val flash: IFlash) : CompletableUseCaseWithParameter<FlashMode> {
    override fun execute(parameter: FlashMode): Completable = flash.mode(parameter)
}