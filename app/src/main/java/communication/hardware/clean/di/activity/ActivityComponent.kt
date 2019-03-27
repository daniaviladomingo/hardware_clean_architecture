package communication.hardware.clean.di.activity

import communication.hardware.clean.di.activity.module.*
import communication.hardware.clean.ui.MainActivity
import communication.hardware.clean.di.application.ApplicationComponent
import dagger.Component

@ActivityScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [
        ActivityModule::class,
        CameraModule::class,
        LocationModule::class,
        NfcModule::class,
        SensorModule::class,
        SmsModule::class,
        UseCaseModule::class,
        ViewModelFactoryModule::class,
        ViewModelModule::class
    ]
)

interface ActivityComponent : ActivityComponentExposes {

    fun inject(mainActivity: MainActivity)

    companion object {
        fun init(daggerActivity: DaggerActivity, applicationComponent: ApplicationComponent): ActivityComponent =
            DaggerActivityComponent.builder()
                .applicationComponent(applicationComponent)
                .activityModule(ActivityModule(daggerActivity))
                .build()
    }
}