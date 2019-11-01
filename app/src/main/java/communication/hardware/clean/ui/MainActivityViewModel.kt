package communication.hardware.clean.ui

import android.graphics.Bitmap
import communication.hardware.clean.SingleLiveEvent
import communication.hardware.clean.base.BaseViewModel
import communication.hardware.clean.domain.interactor.ShakingUseCase
import communication.hardware.clean.domain.interactor.TakePictureUseCase
import communication.hardware.clean.domain.interactor.flash.GetFlashModeUseCase
import communication.hardware.clean.domain.interactor.flash.SetFlashModeUseCase
import communication.hardware.clean.domain.interactor.location.GetLocationUseCase
import communication.hardware.clean.domain.interactor.location.GetLocationsUseCase
import communication.hardware.clean.domain.interactor.location.StopLocationsUseCase
import communication.hardware.clean.domain.interactor.nfc.IsNfcSupportedUseCase
import communication.hardware.clean.domain.interactor.nfc.ReadNfcUseCase
import communication.hardware.clean.domain.interactor.sms.GetSmsUseCase
import communication.hardware.clean.domain.interactor.sms.SendSmsUseCase
import communication.hardware.clean.domain.location.model.Location
import communication.hardware.clean.domain.sms.model.Sms
import communication.hardware.clean.model.mapper.NfcMapper
import communication.hardware.clean.model.mapper.PictureMapper
import communication.hardware.clean.model.mapper.ShakeMapper
import communication.hardware.clean.model.mapper.UiFlashModeMapper
import communication.hardware.clean.schedulers.IScheduleProvider
import communication.hardware.clean.ui.data.Resource

class MainActivityViewModel(
    private val getLocationUseCase: GetLocationUseCase,
    private val getLocationsUseCase: GetLocationsUseCase,
    private val stopLocationsUseCase: StopLocationsUseCase,
    private val getSmsUseCase: GetSmsUseCase,
    private val sendSmsUseCase: SendSmsUseCase,
    private val shakinUseCase: ShakingUseCase,
    private val takePictureUseCase: TakePictureUseCase,
    private val setFlashModeUseCase: SetFlashModeUseCase,
    private val getFlashModeUseCase: GetFlashModeUseCase,
    private val isNfcSupportedUseCase: IsNfcSupportedUseCase,
    private val readNfcUseCase: ReadNfcUseCase,
    private val scheduleProvider: IScheduleProvider,
    private val shakeMapper: ShakeMapper,
    private val nfcMapper: NfcMapper,
    private val flashModeMapper: UiFlashModeMapper,
    private val pictureMapper: PictureMapper
) : BaseViewModel() {

    val locationUseCaseLiveData = SingleLiveEvent<Resource<Location>>()
    val smsUseCaseLiveData = SingleLiveEvent<Resource<Sms>>()
    val shakeLiveData = SingleLiveEvent<Resource<String>>()
    var takePictureLiveData = SingleLiveEvent<Resource<Bitmap>>()
    var flashModeLiveData = SingleLiveEvent<Resource<Boolean>>()
    var readNfcTagLiveData = SingleLiveEvent<Resource<String>>()
    var isNfcSupportedLiveData = SingleLiveEvent<Resource<Boolean>>()

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

    fun stopLocations() {
        addDisposable(stopLocationsUseCase.execute()
            .observeOn(scheduleProvider.ui())
            .subscribeOn(scheduleProvider.io())
            .subscribe({}) {})
    }

    fun getSms() {
        addDisposable(getSmsUseCase.execute()
            .observeOn(scheduleProvider.ui())
            .subscribeOn(scheduleProvider.io())
            .subscribe({ sms ->
                smsUseCaseLiveData.value = Resource.success(sms)
            }) {
                smsUseCaseLiveData.value = Resource.error(it.localizedMessage)
            })
    }

    fun sendSms(sms: Sms) {
        smsUseCaseLiveData.value = Resource.loading()
        addDisposable(sendSmsUseCase.execute(sms)
            .observeOn(scheduleProvider.ui())
            .subscribeOn(scheduleProvider.io())
            .subscribe({
                smsUseCaseLiveData.value = Resource.success(sms)
            }) {
                smsUseCaseLiveData.value = Resource.error(it.localizedMessage)
            })
    }

    fun isShaking() {
        addDisposable(shakinUseCase.execute()
            .observeOn(scheduleProvider.ui())
            .subscribeOn(scheduleProvider.io())
            .subscribe({
                shakeLiveData.value = Resource.success(shakeMapper.map(Unit))
            }) {
                shakeLiveData.value = Resource.error(it.localizedMessage)
            })
    }

    fun takePicture() {
        takePictureLiveData.value = Resource.loading()
        addDisposable(takePictureUseCase.execute()
            .subscribe({ picture ->
                takePictureLiveData.value = Resource.success(pictureMapper.map(picture))
            }) {
                takePictureLiveData.value = Resource.error(it.localizedMessage)
            })
    }

    fun readNfcTag() {
        addDisposable(readNfcUseCase.execute()
            .observeOn(scheduleProvider.ui())
            .subscribeOn(scheduleProvider.io())
            .subscribe({ idTag ->
                readNfcTagLiveData.value = Resource.success(nfcMapper.map(idTag))
            }) {
                readNfcTagLiveData.value = Resource.error(it.localizedMessage)
            })
    }
}