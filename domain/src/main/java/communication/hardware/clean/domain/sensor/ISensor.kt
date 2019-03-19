package communication.hardware.clean.domain.sensor

import io.reactivex.Observable

interface ISensor {
    fun shaking(): Observable<Unit>
}