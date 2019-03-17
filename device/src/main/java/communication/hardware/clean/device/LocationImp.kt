package communication.hardware.clean.device

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import communication.hardware.clean.device.util.isPermissionGranted
import communication.hardware.clean.domain.location.ILocation
import communication.hardware.clean.domain.location.model.Location
import io.reactivex.Completable
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

    private var getOnlyOneLocation = false
    private var locating = false

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
    @Synchronized
    fun start() {
        if (!locating) {
            if (!context.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
                throw IllegalAccessError("${Manifest.permission.ACCESS_FINE_LOCATION} do not granted")
            }
            FusedLocationProviderClient(context).requestLocationUpdates(locationRequest, locationCallback, null)
            locating = true
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    @Synchronized
    fun stop() {
        if (!locating) {
            FusedLocationProviderClient(context).removeLocationUpdates(locationCallback)
            locating = false
        }
    }

    @SuppressLint("MissingPermission")
    override fun getLocation(): Single<Location> = Single.create { emitter ->
        getOnlyOneLocation = true
        start()
        rxPipe = { location ->
            if (getOnlyOneLocation) {
                stop()
            }
            emitter.onSuccess(location)
        }
    }

    @SuppressLint("MissingPermission")
    override fun getLocations(): Observable<Location> = Observable.create { emitter ->
        start()
        rxPipe = { location ->
            emitter.onNext(location)
        }
    }

    override fun stopLocations(): Completable = Completable.create {
        stop()
    }
}