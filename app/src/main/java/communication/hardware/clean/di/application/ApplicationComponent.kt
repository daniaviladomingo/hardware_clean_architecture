package communication.hardware.clean.di.application

import communication.hardware.clean.AppApplication
import communication.hardware.clean.di.application.module.ApplicationModule
import communication.hardware.clean.di.application.module.HardwareModule
import communication.hardware.clean.di.application.module.SchedulerModule
import communication.hardware.clean.di.application.module.UseCaseModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        HardwareModule::class,
        SchedulerModule::class,
        UseCaseModule::class]
)

interface ApplicationComponent : ApplicationComponentExposes {

    companion object {
        fun init(appApplication: AppApplication): ApplicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(appApplication))
            .build()
    }
}