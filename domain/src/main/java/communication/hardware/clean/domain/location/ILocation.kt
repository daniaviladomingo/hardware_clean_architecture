package communication.hardware.clean.domain.location

import communication.hardware.clean.domain.IHardwareSupported
import communication.hardware.clean.domain.location.model.Location
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface ILocation : IHardwareSupported {
    fun getLocations(): Observable<Location>
    fun stopLocations(): Completable
}