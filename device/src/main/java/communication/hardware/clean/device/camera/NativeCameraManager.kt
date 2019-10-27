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
import avila.domingo.lifecycle.ILifecycleObserver
import communication.hardware.clean.device.camera.model.mapper.CameraSideMapper
import communication.hardware.clean.device.exception.IllegalHardwareException
import communication.hardware.clean.device.util.isPermissionGranted
import communication.hardware.clean.domain.camera.model.CameraSide
import kotlin.math.abs

class NativeCameraManager(
    context: Context,
    private val cameraSideMapper: CameraSideMapper,
    private val windowManager: WindowManager,
    private val rangePicture: IntRange,
    private val surfaceView: SurfaceView,
    private val cameraSide: CameraSide
) : INativeCamera, ICameraSide, ILifecycleObserver {

    private lateinit var currentCamera: Camera

    private val surfaceHolderCallback = object : SurfaceHolder.Callback {
        override fun surfaceChanged(
            holder: SurfaceHolder,
            format: Int,
            width: Int,
            height: Int
        ) {
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {}

        override fun surfaceCreated(holder: SurfaceHolder) {
            currentCamera.setPreviewDisplay(holder)
        }
    }

    init {
        if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
            (cameraSide == CameraSide.FRONT && !context.packageManager.hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FRONT
            ))
        ) {
            throw IllegalHardwareException("Device hasn't CAMERA feature")
        }
        if (context.isPermissionGranted(Manifest.permission.CAMERA)) {
            throw IllegalAccessError("${Manifest.permission.CAMERA} permission not granted")
        }
    }

    override fun camera(): Camera = currentCamera

    override fun cameraSide(): CameraSide = cameraSide

    private fun openCamera(cameraSide: CameraSide) {
        currentCamera = Camera.open(cameraSideMapper.map(cameraSide))
        configure()
    }

    private fun configure() {
        currentCamera.run {

            val screenSize = screenSize()

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

            val screenRatio = screenSize.witdh / screenSize.height.toFloat()

            var diff = Float.MAX_VALUE
            var previewWidth = 0
            var previewHeight = 0

            customParameters.supportedPreviewSizes
                .sortedBy { it.width }
                .filter {
                    it.width in rangePicture
                }
                .apply {
                    this.forEach {
                        val previewDiff =
                            abs((it.width / it.height.toFloat()) - screenRatio)
                        if (previewDiff < diff) {
                            diff = previewDiff
                            previewWidth = it.width
                            previewHeight = it.height
                        }
                    }
                }
                .filter { screenRatio == (it.width / it.height.toFloat()) }
                .run {
                    if (size > 1) {
                        get(1).let { customParameters.setPreviewSize(it.width, it.height) }
                    } else {
                        customParameters.setPreviewSize(previewWidth, previewHeight)
                    }
                }

            diff = Float.MAX_VALUE
            previewWidth = 0
            previewHeight = 0

            customParameters.supportedPictureSizes
                .sortedBy { it.width }
                .filter {
                    it.width in rangePicture
                }
                .apply {
                    this.forEach {
                        val previewDiff =
                            abs((it.width / it.height.toFloat()) - screenRatio)
                        if (previewDiff < diff) {
                            diff = previewDiff
                            previewWidth = it.width
                            previewHeight = it.height
                        }
                    }
                }
                .filter { screenRatio == (it.width / it.height.toFloat()) }
                .run {
                    if (size > 1) {
                        get(1).let { customParameters.setPictureSize(it.width, it.height) }
                    } else {
                        customParameters.setPictureSize(previewWidth, previewHeight)
                    }
                }

            parameters = customParameters

            setDisplayOrientation(getCameraDisplayOrientation())
        }
    }

    private fun getCameraDisplayOrientation(): Int {
        val info = Camera.CameraInfo()
        Camera.getCameraInfo(cameraSideMapper.map(cameraSide), info)

        val degrees = when (windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> 0
        }

        return if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            (360 - (info.orientation + degrees) % 360) % 360  // compensate the mirror
        } else {  // back-facing
            (info.orientation - degrees + 360) % 360
        }
    }

    private fun screenSize(): Size =
        Point().apply { windowManager.defaultDisplay.getSize(this) }.let { point ->
            if (point.x > point.y) {
                Size(point.x, point.y)
            } else {
                Size(point.y, point.x)
            }
        }

    internal data class Size(val witdh: Int, val height: Int)

    override fun resume() {
        surfaceView.holder.addCallback(surfaceHolderCallback)
        openCamera(cameraSide)
        currentCamera.setPreviewDisplay(surfaceView.holder)
        currentCamera.startPreview()
    }

    override fun pause() {
        surfaceView.holder.removeCallback(surfaceHolderCallback)
        currentCamera.stopPreview()
    }
}