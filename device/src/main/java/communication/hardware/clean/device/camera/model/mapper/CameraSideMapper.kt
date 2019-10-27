package communication.hardware.clean.device.camera.model.mapper

import android.hardware.Camera
import communication.hardware.clean.domain.camera.model.CameraSide
import communication.hardware.clean.domain.model.mapper.Mapper

class CameraSideMapper : Mapper<CameraSide, Int>() {
    override fun map(model: CameraSide): Int = model.run {
        when (this) {
            CameraSide.BACK -> Camera.CameraInfo.CAMERA_FACING_BACK
            CameraSide.FRONT -> Camera.CameraInfo.CAMERA_FACING_FRONT
        }
    }

    override fun inverseMap(model: Int): CameraSide {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}