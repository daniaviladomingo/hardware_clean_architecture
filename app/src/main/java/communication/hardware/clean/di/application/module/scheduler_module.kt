package communication.hardware.clean.di.application.module

import communication.hardware.clean.schedulers.IScheduleProvider
import communication.hardware.clean.schedulers.ScheduleProviderImp
import org.koin.dsl.module

val scheduleModule = module {
    single<IScheduleProvider> { ScheduleProviderImp() }
}