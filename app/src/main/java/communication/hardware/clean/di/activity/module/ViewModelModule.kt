package communication.hardware.clean.di.activity.module

import androidx.lifecycle.ViewModel
import communication.hardware.clean.di.activity.ActivityScope
import communication.hardware.clean.di.activity.ViewModelKey
import communication.hardware.clean.domain.interactor.*
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
        getLocationsUseCase: GetLocationsUseCase
//        getSmsUseCase: GetSmsUseCase,
//        sendSmsUseCase: SendSmsUseCase,
//        takePictureUseCase: TakePictureUseCase
    ): ViewModel = MainActivityViewModel(
        getLocationUseCase,
        getLocationsUseCase
//        getSmsUseCase,
//        sendSmsUseCase,
//        takePictureUseCase
    )
}