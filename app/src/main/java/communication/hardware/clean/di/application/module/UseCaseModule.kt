package communication.hardware.clean.di.application.module

import communication.hardware.clean.domain.camera.ICamera
import communication.hardware.clean.domain.email.IEmail
import communication.hardware.clean.domain.interactor.*
import communication.hardware.clean.domain.location.ILocation
import communication.hardware.clean.domain.sms.ISms
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UseCaseModule {

    @Provides
    @Singleton
    fun provideGetLocationsUseCase(location: ILocation): GetLocationsUseCase = GetLocationsUseCase(location)

    @Provides
    @Singleton
    fun provideGetLocationUseCase(location: ILocation): GetLocationUseCase = GetLocationUseCase(location)

    @Provides
    @Singleton
    fun provideGetSmsUseCase(sms: ISms): GetSmsUseCase = GetSmsUseCase(sms)

    @Provides
    @Singleton
    fun providesSendEmailUseCase(email: IEmail): SendEmailUseCase = SendEmailUseCase(email)

    @Provides
    @Singleton
    fun provideSendSmsUseCase(sms: ISms): SendSmsUseCase = SendSmsUseCase(sms)

    @Provides
    @Singleton
    fun provideTakePictureUseCase(camera: ICamera): TakePictureUseCase = TakePictureUseCase(camera)

}