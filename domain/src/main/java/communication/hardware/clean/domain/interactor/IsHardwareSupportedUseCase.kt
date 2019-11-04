package communication.hardware.clean.domain.interactor

import communication.hardware.clean.domain.IHardwareSupported
import communication.hardware.clean.domain.interactor.type.SingleUseCase
import io.reactivex.Single

class IsHardwareSupportedUseCase(private val hardwareSupported: IHardwareSupported): SingleUseCase<Boolean> {
    override fun execute(): Single<Boolean> = hardwareSupported.isSupported()
}