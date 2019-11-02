package communication.hardware.clean.device

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import avila.domingo.lifecycle.ILifecycleObserver
import communication.hardware.clean.domain.sensor.ISensor
import io.reactivex.Observable
import io.reactivex.Single
import kotlin.math.abs

class SensorImp(
    private val sensorManager: SensorManager,
    private val samplingPeriodUs: Int,
    private val shakeThreshold: Int
) : ISensor, ILifecycleObserver {

    private val sensorAccelerometer: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var lastUpdate: Long = 0
    private var lastX: Float = 0f
    private var lastY: Float = 0f
    private var lastZ: Float = 0f

    private var rxPipe: () -> Unit = {}

    private val accelerometerSensorListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val curTime = System.currentTimeMillis()

            if (curTime - lastUpdate > 200) {
                val diffTime = curTime - lastUpdate
                lastUpdate = curTime

                val speed = abs(x + y + z - lastX - lastY - lastZ) / diffTime * 5000

                if (speed > shakeThreshold) {
                    rxPipe.invoke()
                }

                lastX = x
                lastY = y
                lastZ = z
            }
        }
    }

    override fun shaking(): Observable<Unit> = Observable.create {
        rxPipe = {
            it.onNext(Unit)
        }
    }

    override fun isSupported(): Single<Boolean> = Single.create {
        it.onSuccess(sensorAccelerometer?.run { true } ?: false)
    }

    override fun resume() {
        sensorAccelerometer?.run {
            sensorManager.registerListener(
                accelerometerSensorListener,
                this,
                samplingPeriodUs
            )
        }
    }

    override fun pause() {
        sensorManager.unregisterListener(accelerometerSensorListener)
    }
}