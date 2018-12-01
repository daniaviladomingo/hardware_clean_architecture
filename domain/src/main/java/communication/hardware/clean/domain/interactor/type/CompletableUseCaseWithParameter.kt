package communication.hardware.clean.domain.interactor.type

import io.reactivex.Completable

interface CompletableUseCaseWithParameter<P> {
    fun execute(parameter: P): Completable
}