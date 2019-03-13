package communication.hardware.clean.di

import communication.hardware.clean.AppApplication
import communication.hardware.clean.di.activity.ActivityComponent
import communication.hardware.clean.di.activity.DaggerActivity
import communication.hardware.clean.di.application.ApplicationComponent

fun createApplicationComponent(appApplication: AppApplication): ApplicationComponent = ApplicationComponent.init(appApplication)

fun createActivityComponent(daggerActivity: DaggerActivity, appApplication: AppApplication): ActivityComponent = ActivityComponent.init(daggerActivity, appApplication.applicationComponent)