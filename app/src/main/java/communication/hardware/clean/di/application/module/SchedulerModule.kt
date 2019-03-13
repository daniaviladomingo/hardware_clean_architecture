package communication.hardware.clean.di.application.module

import communication.hardware.clean.schedulers.IScheduleProvider
import communication.hardware.clean.schedulers.ScheduleProviderImp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SchedulerModule {
    @Provides
    @Singleton
    fun provideScheduleProvider(): IScheduleProvider = ScheduleProviderImp()

    interface Exposes {
        fun schedule(): IScheduleProvider
    }
}