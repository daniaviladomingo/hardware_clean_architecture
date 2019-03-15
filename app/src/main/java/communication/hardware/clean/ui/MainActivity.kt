package communication.hardware.clean.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import communication.hardware.clean.R
import communication.hardware.clean.di.activity.ActivityComponent
import communication.hardware.clean.di.activity.DaggerActivity
import javax.inject.Inject

class MainActivity : DaggerActivity() {

    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivityViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel::class.java)

        setContentView(R.layout.activity_main)
    }

    override fun inject(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }
}
