@file:Suppress("DEPRECATION")

package communication.hardware.clean.device.camera.cameranative

import android.hardware.Camera

interface INativeCamera {
    fun camera(): Camera
    fun cameraId(): Int
    fun rotationDegrees(): Int
    fun isHardwareSupported(): Boolean
}