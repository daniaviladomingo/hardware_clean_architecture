@file:Suppress("DEPRECATION")

package communication.hardware.clean.di.activity.module

import android.content.Context
import android.view.*
import androidx.lifecycle.Lifecycle
import communication.hardware.clean.device.camera.CameraId
import communication.hardware.clean.device.camera.CameraImp
import communication.hardware.clean.di.activity.ActivityScope
import communication.hardware.clean.di.activity.ForActivity
import communication.hardware.clean.di.application.ForApplication
import communication.hardware.clean.domain.camera.ICamera
import dagger.Module
import dagger.Provides

@Module
class CameraModule {
    @Provides
    @ActivityScope
    fun provideCamera(
        @ForApplication context: Context,
        @ForActivity lifecycle: Lifecycle,
        cameraId: CameraId,
        surfaceView: SurfaceView
    ): ICamera = CameraImp(
        context,
        lifecycle, cameraId,
        surfaceView
    )

    @Provides
    @ActivityScope
    fun provideCameraId(): CameraId = CameraId.BACK

    @Provides
    @ActivityScope
    fun provideSurfaceView(@ForApplication context: Context): SurfaceView = SurfaceView(context).apply {
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    interface Exposes {
        fun surfaceView(): SurfaceView
    }
}