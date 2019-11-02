package communication.hardware.clean.domain

import io.reactivex.Single

interface IHardwareSupported {
    fun isSupported(): Single<Boolean>
}