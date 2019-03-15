@file:Suppress("DEPRECATION")

package communication.hardware.clean.di.application.module

import android.content.Context
import android.hardware.Camera
import android.view.*
import communication.hardware.clean.device.CameraImp
import communication.hardware.clean.di.application.BackCamera
import communication.hardware.clean.di.application.ForApplication
import communication.hardware.clean.di.application.FrontCamera
import communication.hardware.clean.domain.camera.ICamera
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CameraModule {

    @Provides
    @Singleton
    fun provideCamera(
        @BackCamera cameraId: Int,
        windowManager: WindowManager,
        surfaceView: SurfaceView
    ): ICamera = CameraImp(
        cameraId,
        windowManager,
        surfaceView
    )

    @Provides
    @Singleton
    @BackCamera
    fun provideBackCamera(): Int = Camera.CameraInfo.CAMERA_FACING_BACK

    @Provides
    @Singleton
    @FrontCamera
    fun provideFrontCamera(): Int = Camera.CameraInfo.CAMERA_FACING_FRONT

    @Provides
    @Singleton
    fun provideSurfaceView(@ForApplication context: Context): SurfaceView = SurfaceView(context).apply {
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    interface Exposes {
        fun surfaceView(): SurfaceView
    }
}