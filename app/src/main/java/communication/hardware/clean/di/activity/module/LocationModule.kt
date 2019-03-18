package communication.hardware.clean.di.activity.module

import android.content.Context
import androidx.lifecycle.Lifecycle
import com.google.android.gms.location.LocationRequest
import communication.hardware.clean.device.LocationImp
import communication.hardware.clean.di.activity.*
import communication.hardware.clean.di.application.*
import communication.hardware.clean.domain.location.ILocation
import dagger.Module
import dagger.Provides
import java.util.concurrent.TimeUnit

@Module
class LocationModule {
    @Provides
    @ActivityScope
    fun provideLocation(
        @ForApplication context: Context,
        @ForActivity lifecycle: Lifecycle,
        @Interval interval: Long,
        @FastInterval fastInterval: Long,
        @Priority priority: Int,
        @MinAccuracy minAccuracy: Int
    ): ILocation = LocationImp(
        context,
        lifecycle,
        interval,
        fastInterval,
        priority,
        minAccuracy
    )

    @Provides
    @ActivityScope
    @Interval
    fun provideInterval(timeUnit: TimeUnit): Long = timeUnit.toMillis(1)

    @Provides
    @ActivityScope
    @FastInterval
    fun provideFastInterval(timeUnit: TimeUnit): Long = timeUnit.toMillis(1)

    @Provides
    @ActivityScope
    @Priority
    fun providePriority(): Int = LocationRequest.PRIORITY_HIGH_ACCURACY

    @Provides
    @ActivityScope
    @MinAccuracy
    fun provideMinAccuracy(): Int = 200

    @Provides
    @ActivityScope
    fun provideTimeUnit(): TimeUnit = TimeUnit.SECONDS
}