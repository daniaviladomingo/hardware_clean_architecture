package communication.hardware.clean.domain.sensor

import communication.hardware.clean.domain.IHardwareSupported
import io.reactivex.Observable

interface ISensor: IHardwareSupported {
    fun shaking(): Observable<Unit>
}