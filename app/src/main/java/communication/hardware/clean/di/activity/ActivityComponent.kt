package communication.hardware.clean.di.activity

import communication.hardware.clean.MainActivity
import communication.hardware.clean.di.activity.module.ActivityModule
import communication.hardware.clean.di.application.ApplicationComponent
import dagger.Component

@ActivityScope
@Component(
        dependencies = [ApplicationComponent::class],
        modules = [
            ActivityModule::class,
            ViewModelModule::class
        ])

interface ActivityComponent : ActivityComponentExposes {

    fun inject(mainActivity: MainActivity)

    companion object {
        fun init(daggerActivity: DaggerActivity, applicationComponent: ApplicationComponent): ActivityComponent = DaggerActivityComponent.builder()
                .applicationComponent(applicationComponent)
                .activityModule(ActivityModule(daggerActivity))
                .build()
    }
}