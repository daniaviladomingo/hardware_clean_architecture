package communication.hardware.clean.di.activity.module

import android.content.Context
import androidx.lifecycle.Lifecycle
import communication.hardware.clean.device.SmsImp
import communication.hardware.clean.di.activity.ActivityScope
import communication.hardware.clean.di.activity.ForActivity
import communication.hardware.clean.di.application.*
import communication.hardware.clean.domain.sms.ISms
import dagger.Module
import dagger.Provides

@Module
class SmsModule {
    @Provides
    @ActivityScope
    fun provideSms(
        @ForApplication context: Context,
        @ForActivity lifecycle: Lifecycle
    ): ISms = SmsImp(
        context,
        lifecycle
    )
}