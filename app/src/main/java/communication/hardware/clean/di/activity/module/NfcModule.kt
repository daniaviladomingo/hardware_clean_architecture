package communication.hardware.clean.di.activity.module

import android.app.Activity
import android.nfc.NfcAdapter
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import communication.hardware.clean.device.NfcImp
import communication.hardware.clean.di.activity.ActivityScope
import communication.hardware.clean.domain.nfc.INfc
import dagger.Module
import dagger.Provides

@Module
class NfcModule {
    @Provides
    @ActivityScope
    fun provideSensorHardware(
        activity: Activity,
        @ActivityScope lifecycle: Lifecycle,
        flags: Int,
        extras: Bundle
    ): INfc = NfcImp(
        activity,
        lifecycle,
        flags,
        extras
    )

    @Provides
    @ActivityScope
    fun provideFlags(): Int = NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK or NfcAdapter.FLAG_READER_NFC_B

    @Provides
    @ActivityScope
    fun providesExtras(): Bundle = Bundle().apply {
        putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 1000)
    }
}

