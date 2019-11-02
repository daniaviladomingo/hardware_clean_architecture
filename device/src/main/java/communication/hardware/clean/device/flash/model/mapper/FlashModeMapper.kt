@file:Suppress("DEPRECATION")

package communication.hardware.clean.device.flash.model.mapper

import android.hardware.Camera
import communication.hardware.clean.domain.flash.model.FlashMode
import communication.hardware.clean.domain.model.mapper.Mapper

class FlashModeMapper : Mapper<FlashMode, String>() {
    override fun map(model: FlashMode): String = when (model) {
        FlashMode.OFF -> Camera.Parameters.FLASH_MODE_OFF
        FlashMode.ON -> Camera.Parameters.FLASH_MODE_TORCH
    }

    override fun inverseMap(model: String): FlashMode = when (model) {
        Camera.Parameters.FLASH_MODE_OFF -> FlashMode.OFF
        Camera.Parameters.FLASH_MODE_TORCH -> FlashMode.ON
        else -> FlashMode.OFF
    }
}