package communication.hardware.clean.device

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import avila.domingo.lifecycle.ILifecycleObserver
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import communication.hardware.clean.device.exception.IllegalHardwareException
import communication.hardware.clean.device.util.isPermissionGranted
import communication.hardware.clean.domain.location.ILocation
import communication.hardware.clean.domain.location.model.Location
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class LocationImp(
    private val context: Context,
    private val interval: Long,
    private val fastestInterval: Long,
    private val priority: Int,
    private val minAccuracy: Int
) : ILocation, ILifecycleObserver {

    private var locating = false
    private var initLocating = false

    private var rxPipe: (Location) -> Unit = {}

    private val handler = Handler()

    private val locationRequest = LocationRequest().apply {
        interval = this@LocationImp.interval
        fastestInterval = this@LocationImp.fastestInterval
        priority = this@LocationImp.priority
    }

//    init {
//        if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION)) {
//            throw IllegalHardwareException("Device hasn't LOCATION feature")
//        }
//    }

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

    @Synchronized
    override fun resume() {
        if (!locating && initLocating) {
            if (!context.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
                throw IllegalAccessError("${Manifest.permission.ACCESS_FINE_LOCATION} do not granted")
            }
            handler.post {
                FusedLocationProviderClient(context).requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    null
                )
            }
            locating = true
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    @Synchronized
    override fun pause() {
        if (locating) {
            FusedLocationProviderClient(context).removeLocationUpdates(locationCallback)
            locating = false
        }
    }

    @SuppressLint("MissingPermission")
    override fun getLocation(): Single<Location> = Single.create { emitter ->
        initLocating = true
        resume()
        rxPipe = { location ->
            pause()
            emitter.onSuccess(location)
        }
    }

    @SuppressLint("MissingPermission")
    override fun getLocations(): Observable<Location> = Observable.create { emitter ->
        initLocating = true
        resume()
        rxPipe = { location ->
            emitter.onNext(location)
        }
    }

    override fun stopLocations(): Completable = Completable.create {
        pause()
    }
}