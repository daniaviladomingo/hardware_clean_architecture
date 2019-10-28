package communication.hardware.clean.domain.interactor

import communication.hardware.clean.domain.camera.ICamera
import communication.hardware.clean.domain.camera.model.Picture
import communication.hardware.clean.domain.interactor.type.SingleUseCase
import io.reactivex.Single

class TakePictureUseCase(private val camera: ICamera) : SingleUseCase<Picture> {
    override fun execute(): Single<Picture> = camera.takePicture()
}