package communication.hardware.clean.domain.interactor.location

import communication.hardware.clean.domain.interactor.type.ObservableUseCase
import communication.hardware.clean.domain.location.ILocation
import communication.hardware.clean.domain.location.model.Location
import io.reactivex.Observable

class GetLocationsUseCase(private val location: ILocation) : ObservableUseCase<Location> {
    override fun execute(): Observable<Location> = location.getLocations()
}