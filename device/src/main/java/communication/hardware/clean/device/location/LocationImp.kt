package communication.hardware.clean.device.location

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import avila.domingo.lifecycle.ILifecycleObserver
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import communication.hardware.clean.device.location.mode.Accuracy
import communication.hardware.clean.device.location.mode.mapper.AccuracyMapper
import communication.hardware.clean.domain.location.ILocation
import communication.hardware.clean.domain.location.model.Location
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class LocationImp(
    private val context: Context,
    private val interval: Long,
    private val fastestInterval: Long,
    private val priority: Accuracy,
    private val minAccuracy: Int,
    private val accuracyMapper: AccuracyMapper
) : ILocation, ILifecycleObserver {

    private var initLocating = false

    private var rxPipe: (Location) -> Unit = {}

    private val handler = Handler()

    private val locationRequest = LocationRequest().apply {
        interval = this@LocationImp.interval
        fastestInterval = this@LocationImp.fastestInterval
        priority = accuracyMapper.map(this@LocationImp.priority)
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
    override fun getLocations(): Observable<Location> = Observable.create { emitter ->
        if (!initLocating) {
            initLocating = true
            init()
            rxPipe = { location ->
                emitter.onNext(location)
            }
        }
    }

    override fun isSupported(): Single<Boolean> = Single.create {
        it.onSuccess(context.packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION))
    }

    override fun stopLocations(): Completable = Completable.create {
        stop()
        initLocating = false
    }

    private fun init() {
        if (initLocating) {
            handler.post {
                FusedLocationProviderClient(context).requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    null
                )
            }
        }
    }

    private fun stop() {
        if (initLocating) {
            FusedLocationProviderClient(context).removeLocationUpdates(locationCallback)
        }
    }

    override fun resume() {
        init()
    }

    override fun pause() {
        stop()
    }
}