package communication.hardware.clean.device.camera

import communication.hardware.clean.domain.camera.model.CameraSide


interface ICameraSide {
    fun cameraSide(): CameraSide
}