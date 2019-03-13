package communication.hardware.clean.di.activity

import communication.hardware.clean.di.activity.module.ActivityModule
import communication.hardware.clean.di.application.ApplicationComponentExposes

interface ActivityComponentExposes :
        ApplicationComponentExposes,
        ActivityModule.Exposes