package communication.hardware.clean.ui

import android.Manifest
import android.os.Bundle
import android.view.SurfaceView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import communication.hardware.clean.R
import communication.hardware.clean.base.BaseActivity
import communication.hardware.clean.device.util.hasNFCFeature
import communication.hardware.clean.device.util.isPermissionGranted
import communication.hardware.clean.device.util.requestPermission
import communication.hardware.clean.device.util.toast
import communication.hardware.clean.di.activity.ActivityComponent
import communication.hardware.clean.domain.sms.model.Sms
import communication.hardware.clean.ui.data.ResourceState
import communication.hardware.clean.util.isPermissionsGranted
import communication.hardware.clean.util.log
import communication.hardware.clean.util.rotate
import communication.hardware.clean.util.toBitmap
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity() {
    private val REQUEST_PERMISSIONS_CODE = 1

    @Inject
    protected lateinit var surfaceView: SurfaceView

    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivityViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel::class.java)

        setListener()

        button_take_photo.setOnClickListener {
            mainActivityViewModel.takePicture()
        }

        button_get_one_location.setOnClickListener {
            mainActivityViewModel.getLocation()
        }

        button_get_locations.setOnClickListener {
            mainActivityViewModel.getLocations()
        }

        button_stop_locations.setOnClickListener {
            mainActivityViewModel.stopLocations()
        }

        button_send_sms.setOnClickListener {
            mainActivityViewModel.sendSms(Sms(destinationAddress.text.toString(), message.text.toString()))
        }

        button_read_sms.setOnClickListener {
            mainActivityViewModel.getSms()
        }

        mainActivityViewModel.isShaking()
        if (hasNFCFeature()) {
            mainActivityViewModel.readNfcTag()
        } else {
            id_nfc_tag.text = "Device hasn't NFC feature !!!"
        }
    }

    override fun onResume() {
        super.onResume()
        surface_view.addView(surfaceView)
        if (!isPermissionGranted(listOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECEIVE_SMS))) {
            requestPermission(listOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECEIVE_SMS), REQUEST_PERMISSIONS_CODE)
        }
    }

    override fun onPause() {
        super.onPause()
        surface_view.removeView(surfaceView)
    }

    private fun setListener() {
        mainActivityViewModel.locationUseCaseLiveData.observe(this, Observer { resource ->
            resource?.run {
                managementResourceState(status, message)
                if (status == ResourceState.SUCCESS) {
                    data?.run {
                        location_result.text =
                            String.format("(lat: %.6f, lon: %.6f, acc: %.2f)", this.latitude, this.longitude, this.accuracy)
                    }
                }
            }
        })

        mainActivityViewModel.smsUseCaseLiveData.observe(this, Observer { resource ->
            resource?.run {
                managementResourceState(status, message)
                if (status == ResourceState.SUCCESS) {
                    data?.run {
                        sms_read.text = this.text
                    }
                }
            }
        })

        mainActivityViewModel.shakeLiveData.observe(this, Observer { resource ->
            resource?.run {
                managementResourceState(status, message)
                if (status == ResourceState.SUCCESS) {
                    data?.run {
                        toast("Shaked !!")
                        shaked_at.text = String.format("Shaked at: %s", this)
                    }
                }
            }
        })

        mainActivityViewModel.takePictureLiveData.observe(this, Observer { resource ->
            resource?.run {
                managementResourceState(status, message)
                if (status == ResourceState.SUCCESS) {
                    data?.run {
                        image_picture.setImageBitmap(picture.toBitmap().rotate(degrees.toFloat()))
                    }
                }
            }
        })

        mainActivityViewModel.readNfcTagLiveData.observe(this, Observer { resource ->
            resource?.run {
                managementResourceState(status, message)
                if (status == ResourceState.SUCCESS) {
                    data?.run {
                        toast("NFC Tag Readed !!")
                        id_nfc_tag.text = this
                    }
                }
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSIONS_CODE -> {
                if (!grantResults.isPermissionsGranted()) {
                    finish()
                }
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun checkAgain(): () -> Unit = {}

    override fun tryAgain(): () -> Unit = {}

    override fun inject(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }
}
