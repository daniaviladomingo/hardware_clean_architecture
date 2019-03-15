package communication.hardware.clean.di.activity.module

import communication.hardware.clean.di.activity.ActivityScope
import communication.hardware.clean.domain.camera.ICamera
import communication.hardware.clean.domain.interactor.*
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

//    @Provides
//    @Singleton
//    fun provideGetSmsUseCase(sms: ISms): GetSmsUseCase = GetSmsUseCase(sms)
//
//    @Provides
//    @Singleton
//    fun provideSendSmsUseCase(sms: ISms): SendSmsUseCase = SendSmsUseCase(sms)
//
//    @Provides
//    @Singleton
//    fun provideTakePictureUseCase(camera: ICamera): TakePictureUseCase = TakePictureUseCase(camera)

    interface Exposes {
        fun getLocationsUseCase(): GetLocationsUseCase
        fun getLocationUseCase(): GetLocationUseCase
//        fun getSmsUseCase(): GetSmsUseCase
//        fun gendSmsUseCase(): SendSmsUseCase
//        fun takePictureUseCase(): TakePictureUseCase
    }
}