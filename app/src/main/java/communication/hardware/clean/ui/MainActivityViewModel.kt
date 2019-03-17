package communication.hardware.clean.ui

import communication.hardware.clean.SingleLiveEvent
import communication.hardware.clean.base.BaseViewModel
import communication.hardware.clean.domain.interactor.location.StopLocationsUseCase
import communication.hardware.clean.domain.interactor.type.CompletableUseCase
import communication.hardware.clean.domain.interactor.type.ObservableUseCase
import communication.hardware.clean.domain.interactor.type.SingleUseCase
import communication.hardware.clean.domain.location.model.Location
import communication.hardware.clean.schedulers.IScheduleProvider
import communication.hardware.clean.ui.data.Resource

class MainActivityViewModel(
    private val getLocationUseCase: SingleUseCase<Location>,
    private val getLocationsUseCase: ObservableUseCase<Location>,
    private val stopLocationsUseCase: CompletableUseCase,
//    private val getSmsUseCase: SingleUseCase<Sms>,
//    private val sendSmsUseCase: CompletableUseCaseWithParameter<Sms>,
//    private val takePictureUseCase: SingleUseCase<Picture>
    private val scheduleProvider: IScheduleProvider
) : BaseViewModel() {

    val locationUseCaseLiveData = SingleLiveEvent<Resource<Location>>()

    fun getLocation() {
        locationUseCaseLiveData.value = Resource.loading()
        addDisposable(getLocationUseCase.execute()
            .observeOn(scheduleProvider.ui())
            .subscribeOn(scheduleProvider.io())
            .subscribe({ location ->
                locationUseCaseLiveData.value = Resource.success(location)
            }) {
                locationUseCaseLiveData.value = Resource.error(it.localizedMessage)
            })
    }

    fun getLocations() {
        locationUseCaseLiveData.value = Resource.loading()
        addDisposable(getLocationsUseCase.execute()
            .observeOn(scheduleProvider.ui())
            .subscribeOn(scheduleProvider.io())
            .subscribe({ location ->
                locationUseCaseLiveData.value = Resource.success(location)
            }) {
                locationUseCaseLiveData.value = Resource.error(it.localizedMessage)
            })
    }

    fun stopLocations(){
        addDisposable(stopLocationsUseCase.execute()
            .observeOn(scheduleProvider.ui())
            .subscribeOn(scheduleProvider.io())
            .subscribe({}) {})
    }
}