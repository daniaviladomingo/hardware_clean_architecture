package communication.hardware.clean

import android.app.Application

class AppApplication : Application() {
    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = createApplicationComponent(this)
    }
}