package communication.hardware.clean.domain.interactor

import communication.hardware.clean.domain.interactor.type.SingleUseCase
import communication.hardware.clean.domain.location.ILocation
import communication.hardware.clean.domain.location.model.Location
import io.reactivex.Single

class GetLocationUseCase(private val location: ILocation) : SingleUseCase<Location> {
    override fun execute(): Single<Location> = location.getLocation()
}