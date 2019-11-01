package communication.hardware.clean

import android.app.Application
import android.util.Log
import communication.hardware.clean.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppApplication)
            androidLogger()
            modules(
                appModule,
                activityModule,
                viewModelModule,
                useCaseModule,
                cameraModule,
                locationModule,
                nfcModule,
                sensorModule,
                smsModule,
                mapperModule,
                scheduleModule
            )
        }
    }
}