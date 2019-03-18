@file:Suppress("DEPRECATION")

package communication.hardware.clean.device.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Point
import android.hardware.Camera
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.WindowManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import communication.hardware.clean.device.exception.IllegalHardwareException
import communication.hardware.clean.device.util.isPermissionGranted
import communication.hardware.clean.domain.camera.ICamera
import communication.hardware.clean.domain.camera.model.Picture
import io.reactivex.Single

class CameraImp(
    private val context: Context,
    lifecycle: Lifecycle,
    private val cameraId: CameraId,
    private val surfaceView: SurfaceView
) : ICamera, LifecycleObserver {

    private var camera: Camera? = null
    private val screenSize: Size
    private val cameraInfo = Camera.CameraInfo()
    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    init {
        if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            throw IllegalHardwareException("Device hasn't CAMERA feature")
        }
        if (cameraId == CameraId.FRONT && !context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            throw IllegalHardwareException("Device hasn't FRONT CAMERA feature")
        }

        lifecycle.addObserver(this)

        screenSize = screenSize()
        Camera.getCameraInfo(cameraId.id, cameraInfo)

        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                try {
                    camera?.setDisplayOrientation(rotationDegrees())
                    camera?.startPreview()
                } catch (e: Exception) {
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                camera?.stopPreview()
            }

            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    camera?.setPreviewDisplay(holder)
                } catch (e: Exception) {
                }
            }
        })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun start() {
        if (!context.isPermissionGranted(Manifest.permission.CAMERA)) {
            throw IllegalAccessError("${Manifest.permission.CAMERA} do not granted")
        }
        try {
            camera = Camera.open(cameraId.id).apply {
                val customParameters = parameters
                customParameters.supportedFocusModes.run {
                    when {
                        this.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE) -> customParameters.focusMode =
                            Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
                        this.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO) -> customParameters.focusMode =
                            Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO
                        this.contains(Camera.Parameters.FOCUS_MODE_AUTO) -> customParameters.focusMode =
                            Camera.Parameters.FOCUS_MODE_AUTO
                    }
                }

                customParameters.supportedPreviewSizes
                    .filter { (screenSize.witdh / screenSize.height.toFloat()) == (it.width / it.height.toFloat()) }
                    .sortedByDescending { it.width }
                    .run {
                        get(if (size > 1) 1 else 0).run {
                            customParameters.setPreviewSize(width, height)
                        }
                    }

                customParameters.supportedPictureSizes
                    .filter { (screenSize.witdh / screenSize.height.toFloat()) == (it.width / it.height.toFloat()) }
                    .sortedByDescending { it.width }
                    .run {
                        get(if (size > 1) 1 else 0).run {
                            customParameters.setPictureSize(width, height)
                        }
                    }

                parameters = customParameters

                setPreviewDisplay(surfaceView.holder)
                setDisplayOrientation(rotationDegrees())
                startPreview()
            }
        } catch (e: RuntimeException) { }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun stop() {
        camera?.stopPreview()
        camera?.release()
        camera = null
    }

    override fun takeImage(): Single<Picture> = Single.create {
        camera?.takePicture(null, null, null,
            { data, camera ->
                camera.startPreview()
                it.onSuccess(Picture(data, rotationDegrees()))
            }
        ) ?: it.onError(Throwable("Camera not initialize"))
    }

    private fun screenSize(): Size =
        Point().apply { windowManager.defaultDisplay.getSize(this) }.let { point ->
            if (point.x > point.y) {
                Size(point.x, point.y)
            } else {
                Size(point.y, point.x)
            }
        }

    private fun rotationDegrees(): Int {
        var rotationDegrees = when (windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            Surface.ROTATION_0 -> 0
            else -> 0
        }

        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
            rotationDegrees = 360 - rotationDegrees
        }

        return (cameraInfo.orientation + rotationDegrees) % 360
    }

    data class Size(val witdh: Int, val height: Int)
}