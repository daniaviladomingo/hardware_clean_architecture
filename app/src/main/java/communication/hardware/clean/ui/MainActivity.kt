package communication.hardware.clean.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import communication.hardware.clean.R
import communication.hardware.clean.base.BaseActivity
import communication.hardware.clean.di.activity.ActivityComponent
import communication.hardware.clean.ui.data.ResourceState
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivityViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel::class.java)

        setListener()

//        mainActivityViewModel.getLocation()
//        mainActivityViewModel.getLocations()

        button_get_one_location.setOnClickListener {
            mainActivityViewModel.getLocation()
        }

        button_get_locations.setOnClickListener {
            mainActivityViewModel.getLocations()
        }

        button_stop_locations.setOnClickListener {
            mainActivityViewModel.stopLocations()
        }
    }

    private fun setListener() {
        mainActivityViewModel.locationUseCaseLiveData.observe(this, Observer { resource ->
            resource?.run {
                managementResourceState(status, message)
                if (status == ResourceState.SUCCESS) {
                    data?.run {
                        location_result.text = "$this"
                    }
                }
            }
        })
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun checkAgain(): () -> Unit = {}

    override fun tryAgain(): () -> Unit = {}

    override fun inject(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }
}
