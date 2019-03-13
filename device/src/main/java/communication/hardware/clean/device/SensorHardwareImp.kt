package communication.hardware.clean.device

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import communication.hardware.clean.domain.sensor.ISensorHardware
import io.reactivex.Completable
import io.reactivex.Observable

class SensorHardwareImp(
        private val sensorManager: SensorManager,
        private val samplingPeriodUs: Int,
        private val shakeThreshold: Int
) : ISensorHardware {

    private val sensorAccelerometer: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var lastUpdate: Long = 0
    private var lastX: Float = 0f
    private var lastY: Float = 0f
    private var lastZ: Float = 0f

    private var rxBus: () -> Unit = {}

    // TODO: asociate with activity/fragment cyclelife
//    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
//    private fun onResume() {
//        sensorManager.registerListener(accelerometerSensorListener, sensorAccelerometer, samplingPeriodUs)
//    }
//
//    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
//    private fun onPause() {
//        sensorManager.unregisterListener(accelerometerSensorListener)
//    }

    override fun enableListener(): Completable = Completable.create {
        sensorManager.registerListener(accelerometerSensorListener, sensorAccelerometer, samplingPeriodUs)
        it.onComplete()
    }

    override fun disableListener(): Completable = Completable.create {
        sensorManager.unregisterListener(accelerometerSensorListener)
        it.onComplete()
    }

    private val accelerometerSensorListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val curTime = System.currentTimeMillis()

            if (curTime - lastUpdate > 500) {
                val diffTime = curTime - lastUpdate
                lastUpdate = curTime

                val speed = Math.abs(x + y + z - lastX - lastY - lastZ) / diffTime * 10000

                if (speed > shakeThreshold) {
                    rxBus.invoke()
                }

                lastX = x
                lastY = y
                lastZ = z
            }
        }
    }

    override fun shaked(): Observable<Unit> = Observable.create {
        rxBus = {
            it.onNext(Unit)
        }
    }
}