package communication.hardware.clean.di.activity.module

import androidx.lifecycle.ViewModel
import communication.hardware.clean.di.activity.ActivityScope
import communication.hardware.clean.di.activity.ViewModelKey
import communication.hardware.clean.domain.interactor.location.GetLocationUseCase
import communication.hardware.clean.domain.interactor.location.GetLocationsUseCase
import communication.hardware.clean.domain.interactor.location.StopLocationsUseCase
import communication.hardware.clean.schedulers.IScheduleProvider
import communication.hardware.clean.ui.MainActivityViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class ViewModelModule {

    @Provides
    @ActivityScope
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    fun provideCameraViewModel(
        getLocationUseCase: GetLocationUseCase,
        getLocationsUseCase: GetLocationsUseCase,
        stopLocationsUseCase: StopLocationsUseCase,
        scheduler: IScheduleProvider
//        getSmsUseCase: GetSmsUseCase,
//        sendSmsUseCase: SendSmsUseCase,
//        takePictureUseCase: TakePictureUseCase
    ): ViewModel = MainActivityViewModel(
        getLocationUseCase,
        getLocationsUseCase,
        stopLocationsUseCase,
//        getSmsUseCase,
//        sendSmsUseCase,
//        takePictureUseCase
        scheduler
    )
}