package communication.hardware.clean.device

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import communication.hardware.clean.domain.sensor.ISensor
import io.reactivex.Observable

class SensorImp(
    context: Context,
    lifecycle: Lifecycle,
    private val samplingPeriodUs: Int,
    private val shakeThreshold: Int
) : ISensor, LifecycleObserver {

    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sensorAccelerometer: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var lastUpdate: Long = 0
    private var lastX: Float = 0f
    private var lastY: Float = 0f
    private var lastZ: Float = 0f

    private var rxPipe: () -> Unit = {}

    init {
        lifecycle.addObserver(this)
    }

    override fun shaking(): Observable<Unit> = Observable.create {
        rxPipe = {
            it.onNext(Unit)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        sensorManager.registerListener(accelerometerSensorListener, sensorAccelerometer, samplingPeriodUs)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        sensorManager.unregisterListener(accelerometerSensorListener)
    }

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

                val speed = Math.abs(x + y + z - lastX - lastY - lastZ) / diffTime * 5000

                if (speed > shakeThreshold) {
                    rxPipe.invoke()
                }

                lastX = x
                lastY = y
                lastZ = z
            }
        }
    }
}