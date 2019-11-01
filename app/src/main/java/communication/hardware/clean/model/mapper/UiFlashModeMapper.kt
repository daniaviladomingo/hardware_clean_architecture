package communication.hardware.clean.model.mapper

import communication.hardware.clean.domain.flash.model.FlashMode
import communication.hardware.clean.domain.model.mapper.Mapper

class UiFlashModeMapper : Mapper<Boolean, FlashMode>() {
    override fun map(model: Boolean): FlashMode = if (model) FlashMode.ON else FlashMode.OFF

    override fun inverseMap(model: FlashMode): Boolean = when (model) {
        FlashMode.ON -> true
        FlashMode.OFF -> false
    }
}