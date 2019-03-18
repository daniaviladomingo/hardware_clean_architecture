package communication.hardware.clean.domain.camera

import communication.hardware.clean.domain.camera.model.Picture
import io.reactivex.Single

interface ICamera {
    fun takeImage(): Single<Picture>
}