@file:Suppress("DEPRECATION")

package communication.hardware.clean.device.camera

import android.hardware.Camera

enum class CameraId constructor(val id: Int) {
    BACK(Camera.CameraInfo.CAMERA_FACING_BACK),
    FRONT(Camera.CameraInfo.CAMERA_FACING_FRONT);

    companion object {
        fun valueFor(id: Int): CameraId = CameraId.values().first { it.id == id }
    }
}