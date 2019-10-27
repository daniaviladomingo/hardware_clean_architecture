@file:Suppress("DEPRECATION")

package communication.hardware.clean.device.camera

import android.hardware.Camera
import android.view.Surface
import android.view.WindowManager
import communication.hardware.clean.device.camera.model.mapper.CameraSideMapper

class CameraRotationUtil(
    private val windowManager: WindowManager,
    private val cameraSide: ICameraSide,
    private val cameraSideMapper: CameraSideMapper
) {
    fun rotationDegrees(): Int {
        val cameraInfo = Camera.CameraInfo()
        Camera.getCameraInfo(cameraSideMapper.map(cameraSide.cameraSide()), cameraInfo)

        var degrees = when (windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            Surface.ROTATION_0 -> 0
            else -> 0
        }

        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
            degrees = 360 - degrees
        }

        return (cameraInfo.orientation + degrees) % 360
    }
}