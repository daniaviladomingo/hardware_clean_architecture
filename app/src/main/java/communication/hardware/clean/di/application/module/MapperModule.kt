package communication.hardware.clean.di.application.module

import communication.hardware.clean.di.application.DatePattern
import communication.hardware.clean.model.mapper.NfcMapper
import communication.hardware.clean.model.mapper.ShakeMapper
import dagger.Module
import dagger.Provides
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Singleton

@Module
class MapperModule {
    @Provides
    @Singleton
    fun provideShakeMapper(@DatePattern datePattern: SimpleDateFormat): ShakeMapper = ShakeMapper(datePattern)

    @Provides
    @Singleton
    @DatePattern
    fun provideDateFormatter(@DatePattern datePattern: String): SimpleDateFormat =
        SimpleDateFormat(datePattern, Locale.getDefault())

    @Provides
    @Singleton
    @DatePattern
    fun provideDatePattern(): String = "HH:mm:ss"

    @Provides
    @Singleton
    fun provideNfcMapper(): NfcMapper = NfcMapper()

    interface Exposes {
        fun shakeMapper(): ShakeMapper
        fun nfcMapper(): NfcMapper
    }
}