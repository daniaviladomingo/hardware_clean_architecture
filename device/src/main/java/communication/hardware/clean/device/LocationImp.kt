package communication.hardware.clean.device

import android.Manifest
import android.annotation.SuppressLint
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import communication.hardware.clean.device.util.isPermissionGranted
import communication.hardware.clean.domain.location.ILocation
import communication.hardware.clean.domain.location.model.Location
import io.reactivex.Observable
import io.reactivex.Single

class LocationImp(
    private val context: Context,
    lifecycle: Lifecycle,
    private val interval: Long,
    private val fastestInterval: Long,
    private val priority: Int,
    private val minAccuracy: Int
) : ILocation, LifecycleObserver {

    private var getLocations = false

    init {
        lifecycle.addObserver(this)
    }

    private var rxPipe: (Location) -> Unit = {}

    private val locationRequest = LocationRequest().apply {
        interval = this@LocationImp.interval
        fastestInterval = this@LocationImp.fastestInterval
        priority = this@LocationImp.priority
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            if (locationResult.lastLocation.accuracy < minAccuracy) {
                rxPipe.invoke(
                    Location(
                        locationResult.lastLocation.latitude,
                        locationResult.lastLocation.longitude,
                        locationResult.lastLocation.accuracy
                    )
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        if (!context.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            throw IllegalAccessError("${Manifest.permission.ACCESS_FINE_LOCATION} do not granted")
        }
        FusedLocationProviderClient(context).requestLocationUpdates(locationRequest, locationCallback, null)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        FusedLocationProviderClient(context).removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    override fun getLocation(): Single<Location> = Single.create { emitter ->
        rxPipe = { location ->
            if (!getLocations) {
                FusedLocationProviderClient(context).removeLocationUpdates(locationCallback)
            }
            emitter.onSuccess(location)
        }
    }

    @SuppressLint("MissingPermission")
    override fun getLocations(): Observable<Location> = Observable.create { emitter ->
        getLocations = true
        rxPipe = { location ->
            emitter.onNext(location)
        }
    }
}