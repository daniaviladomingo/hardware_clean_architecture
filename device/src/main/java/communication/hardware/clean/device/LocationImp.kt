package communication.hardware.clean.device

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import communication.hardware.clean.domain.location.ILocation
import communication.hardware.clean.domain.location.model.Location
import io.reactivex.Observable
import io.reactivex.Single

class LocationImp(private val context: Context): ILocation {
    @SuppressLint("MissingPermission")
    override fun getLocation(): Single<Location> = Single.create {
        val locationRequest = LocationRequest()
        locationRequest.interval = 1000
        locationRequest.fastestInterval = 1000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                Log.d("location", "(${locationResult.lastLocation.latitude},${locationResult.lastLocation.longitude}) -> ${locationResult.lastLocation.accuracy}")
                if (locationResult.lastLocation.accuracy < 100) {
                    FusedLocationProviderClient(context).removeLocationUpdates(this)
                    it.onSuccess(Location(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude, locationResult.lastLocation.accuracy))
                }
            }
        }

        FusedLocationProviderClient(context).requestLocationUpdates(locationRequest, locationCallback, null)
    }

    override fun getLocations(): Observable<Location> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}