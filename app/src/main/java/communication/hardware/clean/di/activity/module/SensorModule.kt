package communication.hardware.clean.di.activity.module

import android.content.Context
import android.hardware.SensorManager
import androidx.lifecycle.Lifecycle
import communication.hardware.clean.device.SensorImp
import communication.hardware.clean.di.activity.*
import communication.hardware.clean.di.application.*
import communication.hardware.clean.domain.sensor.ISensor
import dagger.Module
import dagger.Provides

@Module
class SensorModule {
    @Provides
    @ActivityScope
    fun provideSensor(
        @ForApplication context: Context,
        @ForActivity lifecycle: Lifecycle,
        samplingPeriodUs: Int,
        shakeThreshold: Int
    ): ISensor = SensorImp(
        context,
        lifecycle,
        samplingPeriodUs,
        shakeThreshold
    )

    @Provides
    @ActivityScope
    @SamplingPeriodUs
    fun provideSamplingPeriod(): Int = SensorManager.SENSOR_DELAY_NORMAL

    @Provides
    @ActivityScope
    @ShakeThreshold
    fun provideShakeThreshold(): Int = 600
}