package communication.hardware.clean.domain.interactor.location

import communication.hardware.clean.domain.interactor.type.CompletableUseCase
import communication.hardware.clean.domain.location.ILocation
import io.reactivex.Completable

class StopLocationsUseCase(private val location: ILocation) : CompletableUseCase {
    override fun execute(): Completable = location.stopLocations()
}