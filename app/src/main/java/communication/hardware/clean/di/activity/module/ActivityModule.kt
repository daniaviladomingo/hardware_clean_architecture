package communication.hardware.clean.di.activity.module

import android.app.Activity
import androidx.lifecycle.Lifecycle
import android.content.Context
import androidx.fragment.app.FragmentManager
import communication.hardware.clean.di.activity.ActivityScope
import communication.hardware.clean.di.activity.DaggerActivity
import communication.hardware.clean.di.activity.ForActivity
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val daggerActivity: DaggerActivity) {

    @Provides
    @ActivityScope
    @ForActivity
    fun provideContext(): Context = daggerActivity

    @Provides
    @ActivityScope
    fun provideActivity(): Activity = daggerActivity

    @Provides
    @ActivityScope
    @ForActivity
    fun provideLifecycle(): Lifecycle = daggerActivity.lifecycle

    @Provides
    @ActivityScope
    fun provideFragmentManager(): FragmentManager = daggerActivity.supportFragmentManager
}