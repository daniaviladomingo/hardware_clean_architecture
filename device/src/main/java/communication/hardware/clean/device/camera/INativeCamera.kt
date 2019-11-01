package communication.hardware.clean.device.camera

import android.hardware.Camera

interface INativeCamera {
    fun camera(): Camera
    fun cameraId(): Int
    fun rotationDegrees(): Int
}