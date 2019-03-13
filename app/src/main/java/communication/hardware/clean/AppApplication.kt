package communication.hardware.clean

import android.app.Application
import communication.hardware.clean.di.application.ApplicationComponent
import communication.hardware.clean.di.createApplicationComponent

class AppApplication : Application() {
    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = createApplicationComponent(this)
    }
}