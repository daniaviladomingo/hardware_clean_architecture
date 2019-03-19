package communication.hardware.clean.di.application

import communication.hardware.clean.di.application.module.ApplicationModule
import communication.hardware.clean.di.application.module.MapperModule
import communication.hardware.clean.di.application.module.SchedulerModule

interface ApplicationComponentExposes :
    ApplicationModule.Exposes,
    MapperModule.Exposes,
    SchedulerModule.Exposes