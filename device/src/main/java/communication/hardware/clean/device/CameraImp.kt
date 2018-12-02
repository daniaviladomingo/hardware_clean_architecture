package communication.hardware.clean.device

import android.graphics.Point
import android.hardware.Camera
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.WindowManager
import communication.hardware.clean.domain.camera.ICamera
import communication.hardware.clean.domain.camera.model.Picture
import io.reactivex.Completable
import io.reactivex.Single

class CameraImp(
    private val cameraId: Int,
    private val windowManager: WindowManager,
    private val surfaceView: SurfaceView
) : ICamera {

    private var camera: Camera? = null
    private val screenSize: Size
    private val cameraInfo = Camera.CameraInfo()

    init {
        screenSize = screenSize()
        Camera.getCameraInfo(cameraId, cameraInfo)

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

    override fun open(): Completable = Completable.create { emitter ->
        try {
            camera = Camera.open(cameraId).apply {
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

            emitter.onComplete()
        } catch (e: Exception) {
            emitter.onError(Throwable("Error to open Camera"))
        }
    }

    override fun release(): Completable = Completable.create {
        camera?.stopPreview()
        camera?.release()
        camera = null
        it.onComplete()
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