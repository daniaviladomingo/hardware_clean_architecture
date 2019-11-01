@file:Suppress("DEPRECATION")

package communication.hardware.clean.device.camera.util

import android.hardware.Camera
import android.view.Display
import android.view.Surface
import android.view.WindowManager
import communication.hardware.clean.device.camera.model.mapper.CameraSideMapper

class CameraRotationUtil(
    private val display: Display
) {
    fun rotationDegreesPreview(cameraId: Int): Int {
        val cameraInfo = cameraInfo(cameraId)
        val degrees = degrees()

        return if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            (360 - (cameraInfo.orientation + degrees) % 360) % 360
        } else {
            (cameraInfo.orientation - degrees + 360) % 360
        }
    }

    fun rotationDegreesImage(cameraId: Int): Int {
        val cameraInfo = cameraInfo(cameraId)
        var degrees = degrees()

        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
            degrees = 360 - degrees
        }

        return (cameraInfo.orientation + degrees) % 360
    }

    private fun cameraInfo(cameraId: Int): Camera.CameraInfo {
        val cameraInfo = Camera.CameraInfo()
        Camera.getCameraInfo(cameraId, cameraInfo)
        return cameraInfo
    }

    private fun degrees(): Int = when (display.rotation) {
        Surface.ROTATION_0 -> 0
        Surface.ROTATION_90 -> 90
        Surface.ROTATION_180 -> 180
        Surface.ROTATION_270 -> 270
        else -> 0
    }
}