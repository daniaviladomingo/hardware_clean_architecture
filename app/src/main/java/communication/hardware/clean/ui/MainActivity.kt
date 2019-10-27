package communication.hardware.clean.ui

import android.Manifest
import android.os.Bundle
import android.view.SurfaceView
import androidx.lifecycle.Observer
import communication.hardware.clean.R
import communication.hardware.clean.base.BaseActivity
import communication.hardware.clean.device.util.hasNFCFeature
import communication.hardware.clean.device.util.isPermissionGranted
import communication.hardware.clean.device.util.toast
import communication.hardware.clean.domain.sms.model.Sms
import communication.hardware.clean.ui.data.ResourceState
import communication.hardware.clean.util.extension.isPermissionsGranted
import communication.hardware.clean.util.extension.requestPermission
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainActivity : BaseActivity() {
    private val REQUEST_PERMISSIONS_CODE = 1

    private val lifecycleObserver: Unit by inject { parametersOf(this) }

    protected val surfaceView: SurfaceView by inject()

    private val mainActivityViewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleObserver.run {  }

        if (!isPermissionGranted(
                listOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.RECEIVE_SMS
                )
            )
        ) {
            requestPermission(
                listOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.RECEIVE_SMS
                ), REQUEST_PERMISSIONS_CODE
            )
        } else {
            init()
        }
    }

    private fun init() {
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
            mainActivityViewModel.sendSms(
                Sms(
                    destinationAddress.text.toString(),
                    message.text.toString()
                )
            )
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
                            String.format(
                                "(lat: %.6f, lon: %.6f, acc: %.2f)",
                                this.latitude,
                                this.longitude,
                                this.accuracy
                            )
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
                        image_picture.setImageBitmap(this)
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSIONS_CODE -> {
                if (!grantResults.isPermissionsGranted()) {
                    finish()
                } else {
                    init()
                }
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun checkAgain(): () -> Unit = {}

    override fun tryAgain(): () -> Unit = {}
}
