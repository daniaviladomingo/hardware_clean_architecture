package communication.hardware.clean.ui

import communication.hardware.clean.base.BaseViewModel
import communication.hardware.clean.domain.camera.model.Picture
import communication.hardware.clean.domain.interactor.type.CompletableUseCaseWithParameter
import communication.hardware.clean.domain.interactor.type.ObservableUseCase
import communication.hardware.clean.domain.interactor.type.SingleUseCase
import communication.hardware.clean.domain.location.model.Location
import communication.hardware.clean.domain.sms.model.Sms

class MainActivityViewModel(
    private val getLocationUseCase: SingleUseCase<Location>,
    private val getLocationsUseCase: ObservableUseCase<Location>
//    private val getSmsUseCase: SingleUseCase<Sms>,
//    private val sendSmsUseCase: CompletableUseCaseWithParameter<Sms>,
//    private val takePictureUseCase: SingleUseCase<Picture>
) : BaseViewModel() {
}