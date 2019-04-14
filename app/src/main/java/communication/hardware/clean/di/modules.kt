package communication.hardware.clean.di

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import communication.hardware.clean.device.SmsImp
import communication.hardware.clean.di.qualifiers.DateFormat
import communication.hardware.clean.di.qualifiers.ForActivity
import communication.hardware.clean.di.qualifiers.ForApplication
import communication.hardware.clean.domain.interactor.ReadNfcUseCase
import communication.hardware.clean.domain.interactor.ShakingUseCase
import communication.hardware.clean.domain.interactor.TakePictureUseCase
import communication.hardware.clean.domain.interactor.location.GetLocationUseCase
import communication.hardware.clean.domain.interactor.location.GetLocationsUseCase
import communication.hardware.clean.domain.interactor.location.StopLocationsUseCase
import communication.hardware.clean.domain.interactor.sms.GetSmsUseCase
import communication.hardware.clean.domain.interactor.sms.SendSmsUseCase
import communication.hardware.clean.domain.model.mapper.Mapper
import communication.hardware.clean.model.mapper.NfcMapper
import communication.hardware.clean.model.mapper.ShakeMapper
import communication.hardware.clean.schedulers.IScheduleProvider
import communication.hardware.clean.schedulers.ScheduleProviderImp
import communication.hardware.clean.ui.MainActivityViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.text.SimpleDateFormat
import java.util.*

val appModule = module {
    single<Context>(ForApplication) { (app: Application) -> app }
}

val activityModule = module {
    single { (activity: AppCompatActivity) -> activity.lifecycle }
    single<Context>(ForActivity) { (activity: AppCompatActivity) -> activity }
}

val viewModelModule = module {
    viewModel { MainActivityViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
}

val useCaseModule = module {
    factory { GetLocationsUseCase(get()) }
    factory { GetLocationUseCase(get()) }
    factory { StopLocationsUseCase(get()) }
    factory { GetSmsUseCase(get()) }
    factory { SendSmsUseCase(get()) }
    factory { TakePictureUseCase(get()) }
    factory { ShakingUseCase(get()) }
    factory { ReadNfcUseCase(get()) }
}

val smsModule = module {
    single { SmsImp(get(ForApplication), get()) }
}

@SuppressLint("ConstantLocale")
val mapperModule = module {

    single<Mapper<Unit, String>> { ShakeMapper(get(DateFormat)) }

    single<Mapper<ByteArray, String>> { NfcMapper() }

    single { SimpleDateFormat(get(), Locale.getDefault()) }

    single(DateFormat) { "HH:mm:ss" }
}

val scheduleModule = module {
    single<IScheduleProvider> { ScheduleProviderImp() }
}