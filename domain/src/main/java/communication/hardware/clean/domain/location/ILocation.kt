package communication.hardware.clean.domain.location

import communication.hardware.clean.domain.location.model.Location
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface ILocation {
    fun getLocation(): Single<Location>
    fun getLocations(): Observable<Location>
    fun stopLocations(): Completable
}