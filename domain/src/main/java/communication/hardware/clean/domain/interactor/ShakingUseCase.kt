package communication.hardware.clean.domain.interactor

import communication.hardware.clean.domain.interactor.type.ObservableUseCase
import communication.hardware.clean.domain.sensor.ISensor
import io.reactivex.Observable

class ShakingUseCase(private val sensor: ISensor) : ObservableUseCase<Unit> {
    override fun execute(): Observable<Unit> = sensor.shaking()
}