@file:Suppress("DEPRECATION")

package communication.hardware.clean.device.camera

import communication.hardware.clean.domain.camera.ICamera
import communication.hardware.clean.domain.camera.model.Picture
import io.reactivex.Single

class CameraImp(
    private val nativeCamera: INativeCamera
) : ICamera {
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