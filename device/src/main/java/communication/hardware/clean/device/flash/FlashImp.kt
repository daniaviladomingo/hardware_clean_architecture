@file:Suppress("DEPRECATION")

package communication.hardware.clean.device.flash

import android.content.pm.PackageManager
import communication.hardware.clean.device.camera.cameranative.INativeFlash
import communication.hardware.clean.device.flash.model.mapper.FlashModeMapper
import communication.hardware.clean.domain.flash.IFlash
import communication.hardware.clean.domain.flash.model.FlashMode
import io.reactivex.Completable
import io.reactivex.Single

class FlashImp(
    private val packageManager: PackageManager,
    private val nativeFlash: INativeFlash,
    private val flashModeMapper: FlashModeMapper
) : IFlash {

    override fun mode(mode: FlashMode): Completable = Completable.create {
        nativeFlash.mode(flashModeMapper.map(mode))
        it.onComplete()
    }

    override fun mode(): Single<FlashMode> = Single.create {
        it.onSuccess(flashModeMapper.inverseMap(nativeFlash.mode()))
    }

    override fun isSupported(): Single<Boolean> = Single.create {
        it.onSuccess(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
    }
}