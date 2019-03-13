package communication.hardware.clean.domain.sensor

import io.reactivex.Completable
import io.reactivex.Observable

interface ISensorHardware {
    fun enableListener(): Completable
    fun disableListener(): Completable
    fun shaked(): Observable<Unit>
}