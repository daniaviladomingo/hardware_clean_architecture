package communication.hardware.clean.domain.interactor.type

import io.reactivex.Completable

interface CompletableUseCase {
    fun execute(): Completable
}