package communication.hardware.clean.di.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

abstract class DaggerActivity : AppCompatActivity() {

    var activityComponent: ActivityComponent? = null
        get() = field ?: createActivityComponent(this, appApplication)

    private val appApplication: AppApplication
        get() = (application as AppApplication)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject(activityComponent!!)
    }

    protected abstract fun inject(activityComponent: ActivityComponent)
}