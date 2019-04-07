package communication.hardware.clean.di.application.module

import android.annotation.SuppressLint
import communication.hardware.clean.domain.model.mapper.Mapper
import communication.hardware.clean.model.mapper.NfcMapper
import communication.hardware.clean.model.mapper.ShakeMapper
import org.koin.dsl.module
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("ConstantLocale")
val mapperModule = module {

    single<Mapper<Unit, String>> { ShakeMapper(get()) }

    single<Mapper<ByteArray, String>> { NfcMapper() }

    single { SimpleDateFormat(get(), Locale.getDefault()) }

    single { "HH:mm:ss" }
}