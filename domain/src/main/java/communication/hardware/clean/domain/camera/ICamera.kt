package communication.hardware.clean.domain.camera

import communication.hardware.clean.domain.IHardwareSupported
import communication.hardware.clean.domain.camera.model.Picture
import io.reactivex.Single

interface ICamera : IHardwareSupported {
    fun takePicture(): Single<Picture>
}