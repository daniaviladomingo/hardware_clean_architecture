package communication.hardware.clean.di.activity.module

import communication.hardware.clean.di.activity.ActivityScope
import communication.hardware.clean.domain.camera.ICamera
import communication.hardware.clean.domain.interactor.TakePictureUseCase
import communication.hardware.clean.domain.interactor.location.GetLocationUseCase
import communication.hardware.clean.domain.interactor.location.GetLocationsUseCase
import communication.hardware.clean.domain.interactor.location.StopLocationsUseCase
import communication.hardware.clean.domain.interactor.sms.GetSmsUseCase
import communication.hardware.clean.domain.interactor.sms.SendSmsUseCase
import communication.hardware.clean.domain.location.ILocation
import communication.hardware.clean.domain.sms.ISms
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UseCaseModule {

    @Provides
    @ActivityScope
    fun provideGetLocationsUseCase(location: ILocation): GetLocationsUseCase = GetLocationsUseCase(location)

    @Provides
    @ActivityScope
    fun provideGetLocationUseCase(location: ILocation): GetLocationUseCase = GetLocationUseCase(location)

    @Provides
    @ActivityScope
    fun provideStopLocationsUseCase(location: ILocation): StopLocationsUseCase = StopLocationsUseCase(location)

    @Provides
    @ActivityScope
    fun provideGetSmsUseCase(sms: ISms): GetSmsUseCase = GetSmsUseCase(sms)

    @Provides
    @ActivityScope
    fun provideSendSmsUseCase(sms: ISms): SendSmsUseCase = SendSmsUseCase(sms)

    @Provides
    @ActivityScope
    fun provideTakePictureUseCase(camera: ICamera): TakePictureUseCase = TakePictureUseCase(camera)

    interface Exposes {
//        fun getLocationsUseCase(): GetLocationsUseCase
//        fun getLocationUseCase(): GetLocationUseCase
//        fun stopLocationsUseCase(): StopLocationsUseCase
//        fun getSmsUseCase(): GetSmsUseCase
//        fun gendSmsUseCase(): SendSmsUseCase
//        fun takePictureUseCase(): TakePictureUseCase
    }
}