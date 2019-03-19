package communication.hardware.clean.di.application

import communication.hardware.clean.AppApplication
import communication.hardware.clean.di.application.module.ApplicationModule
import communication.hardware.clean.di.application.module.MapperModule
import communication.hardware.clean.di.application.module.SchedulerModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        MapperModule::class,
        SchedulerModule::class]
)

interface ApplicationComponent : ApplicationComponentExposes {

    companion object {
        fun init(appApplication: AppApplication): ApplicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(appApplication))
            .build()
    }
}