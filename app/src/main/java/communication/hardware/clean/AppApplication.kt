package communication.hardware.clean

import android.app.Application
import communication.hardware.clean.di.application.ApplicationComponent
import communication.hardware.clean.di.createApplicationComponent
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@AppApplication)
        }
    }
}