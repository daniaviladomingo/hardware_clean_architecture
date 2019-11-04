package communication.hardware.clean.ui

import android.graphics.Bitmap
import communication.hardware.clean.SingleLiveEvent
import communication.hardware.clean.base.BaseViewModel
import communication.hardware.clean.domain.interactor.IsHardwareSupportedUseCase
import communication.hardware.clean.domain.interactor.ShakingUseCase
import communication.hardware.clean.domain.interactor.TakePictureUseCase
import communication.hardware.clean.domain.interactor.flash.GetFlashModeUseCase
import communication.hardware.clean.domain.interactor.flash.SetFlashModeUseCase
import communication.hardware.clean.domain.interactor.location.GetLocationsUseCase
import communication.hardware.clean.domain.interactor.location.StopLocationsUseCase
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
    private val getLocationsUseCase: GetLocationsUseCase,
    private val stopLocationsUseCase: StopLocationsUseCase,
    private val isLocationSupportedUseCase: IsHardwareSupportedUseCase,
    private val getSmsUseCase: GetSmsUseCase,
    private val sendSmsUseCase: SendSmsUseCase,
    private val isSmsSupportedUseCase: IsHardwareSupportedUseCase,
    private val shakinUseCase: ShakingUseCase,
    private val isSensorSupportedUseCase: IsHardwareSupportedUseCase,
    private val takePictureUseCase: TakePictureUseCase,
    private val isCameraSupportedUseCase: IsHardwareSupportedUseCase,
    private val setFlashModeUseCase: SetFlashModeUseCase,
    private val getFlashModeUseCase: GetFlashModeUseCase,
    private val isFlashSupportedUseCase: IsHardwareSupportedUseCase,
    private val readNfcUseCase: ReadNfcUseCase,
    private val isNfcSupportedUseCase: IsHardwareSupportedUseCase,
    private val scheduleProvider: IScheduleProvider,
    private val shakeMapper: ShakeMapper,
    private val nfcMapper: NfcMapper,
    private val flashModeMapper: UiFlashModeMapper,
    private val pictureMapper: PictureMapper
) : BaseViewModel() {

    val locationUseCaseLiveData = SingleLiveEvent<Resource<Location>>()
    var isLocationSupportedLiveData = SingleLiveEvent<Resource<Boolean>>()

    val smsUseCaseLiveData = SingleLiveEvent<Resource<Sms>>()
    var isSmsSupportedLiveData = SingleLiveEvent<Resource<Boolean>>()

    val shakeLiveData = SingleLiveEvent<Resource<String>>()
    var isSensorSupportedLiveData = SingleLiveEvent<Resource<Boolean>>()

    var takePictureLiveData = SingleLiveEvent<Resource<Bitmap>>()
    var isCameraSupportedLiveData = SingleLiveEvent<Resource<Boolean>>()
    var flashModeLiveData = SingleLiveEvent<Resource<Boolean>>()
    var isFlashSupportedLiveData = SingleLiveEvent<Resource<Boolean>>()

    var readNfcTagLiveData = SingleLiveEvent<Resource<String>>()
    var isNfcSupportedLiveData = SingleLiveEvent<Resource<Boolean>>()


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

    fun isLocationSupported() {
        addDisposable(isLocationSupportedUseCase.execute()
            .observeOn(scheduleProvider.ui())
            .subscribeOn(scheduleProvider.io())
            .subscribe({ isSupported ->
                isLocationSupportedLiveData.value = Resource.success(isSupported)
            }) {
                isLocationSupportedLiveData.value = Resource.error(it.localizedMessage)
            })
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

    fun isSmsSupported() {
        addDisposable(isSmsSupportedUseCase.execute()
            .observeOn(scheduleProvider.ui())
            .subscribeOn(scheduleProvider.io())
            .subscribe({ isSupported ->
                isSmsSupportedLiveData.value = Resource.success(isSupported)
            }) {
                isSmsSupportedLiveData.value = Resource.error(it.localizedMessage)
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

    fun isSensorSupported() {
        addDisposable(isSensorSupportedUseCase.execute()
            .observeOn(scheduleProvider.ui())
            .subscribeOn(scheduleProvider.io())
            .subscribe({ isSupported ->
                isSensorSupportedLiveData.value = Resource.success(isSupported)
            }) {
                isSensorSupportedLiveData.value = Resource.error(it.localizedMessage)
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

    fun isCameraSupported() {
        addDisposable(isCameraSupportedUseCase.execute()
            .observeOn(scheduleProvider.ui())
            .subscribeOn(scheduleProvider.io())
            .subscribe({ isSupported ->
                isCameraSupportedLiveData.value = Resource.success(isSupported)
            }) {
                isCameraSupportedLiveData.value = Resource.error(it.localizedMessage)
            })
    }

    fun setFlashMode(mode: Boolean) {
        addDisposable(setFlashModeUseCase.execute(flashModeMapper.map(mode))
            .observeOn(scheduleProvider.ui())
            .subscribeOn(scheduleProvider.io())
            .subscribe({}) {})
    }

    fun flashMode() {
        addDisposable(getFlashModeUseCase.execute()
            .observeOn(scheduleProvider.ui())
            .subscribeOn(scheduleProvider.io())
            .subscribe({ mode ->
                flashModeLiveData.value = Resource.success(flashModeMapper.inverseMap(mode))
            }) {
                flashModeLiveData.value = Resource.error(it.localizedMessage)
            })
    }

    fun isFlashSupported() {
        addDisposable(isFlashSupportedUseCase.execute()
            .observeOn(scheduleProvider.ui())
            .subscribeOn(scheduleProvider.io())
            .subscribe({ isSupported ->
                isFlashSupportedLiveData.value = Resource.success(isSupported)
            }) {
                isFlashSupportedLiveData.value = Resource.error(it.localizedMessage)
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

    fun isNfcSupported() {
        addDisposable(isNfcSupportedUseCase.execute()
            .observeOn(scheduleProvider.ui())
            .subscribeOn(scheduleProvider.io())
            .subscribe({ isSupported ->
                isNfcSupportedLiveData.value = Resource.success(isSupported)
            }) {
                isNfcSupportedLiveData.value = Resource.error(it.localizedMessage)
            })
    }
}