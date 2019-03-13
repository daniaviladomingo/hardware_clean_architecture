package communication.hardware.clean.di.application

import communication.hardware.clean.di.application.module.ApplicationModule
import communication.hardware.clean.di.application.module.SchedulerModule
import communication.hardware.clean.di.application.module.UseCaseModule

interface ApplicationComponentExposes :
        ApplicationModule.Exposes,
        SchedulerModule.Exposes,
        UseCaseModule.Exposes
