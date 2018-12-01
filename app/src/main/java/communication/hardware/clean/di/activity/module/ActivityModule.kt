package communication.hardware.clean.di.activity.module

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.content.Context
import android.support.v4.app.FragmentManager
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
    @ForActivity
    fun provideLifecycle(): Lifecycle = daggerActivity.lifecycle

    @Provides
    @ActivityScope
    fun provideActivity(): Activity = daggerActivity

    @Provides
    @ActivityScope
    fun provideFragmentManager(): FragmentManager = daggerActivity.supportFragmentManager

    interface Exposes {
        @ForActivity
        fun context(): Context

        fun activity(): Activity
    }
}