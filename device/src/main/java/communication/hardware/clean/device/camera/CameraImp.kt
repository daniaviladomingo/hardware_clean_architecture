@file:Suppress("DEPRECATION")

package communication.hardware.clean.device.camera

import android.content.Context
import android.content.pm.PackageManager
import communication.hardware.clean.device.camera.cameranative.INativeCamera
import communication.hardware.clean.domain.camera.ICamera
import communication.hardware.clean.domain.camera.model.Picture
import io.reactivex.Single

class CameraImp(
    private val context: Context,
    private val nativeCamera: INativeCamera
) : ICamera {
    override fun isSupported(): Single<Boolean> = Single.create {
        it.onSuccess(
            context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
                    && context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
        )
    }

    override fun takePicture(): Single<Picture> = Single.create {
        nativeCamera.camera().takePicture(null, null, null, { data, camera ->
            camera.startPreview()
            val pictureSize = camera.parameters.pictureSize
            it.onSuccess(
                Picture(
                    data,
                    pictureSize.width,
                    pictureSize.height,
                    nativeCamera.rotationDegrees()
                )
            )
        })
    }
}