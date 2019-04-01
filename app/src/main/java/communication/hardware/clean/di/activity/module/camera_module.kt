package communication.hardware.clean.di.activity.module

import android.view.SurfaceView
import android.view.ViewGroup
import communication.hardware.clean.device.camera.CameraId
import communication.hardware.clean.device.camera.CameraImp
import communication.hardware.clean.domain.camera.ICamera
import org.koin.dsl.module

val cameraModule = module {
//    @Provides
//    @ActivityScope
//    fun provideCamera(
//        @ForApplication context: Context,
//        @ForActivity lifecycle: Lifecycle,
//        cameraId: CameraId,
//        surfaceView: SurfaceView
//    ): ICamera =
//
//        @Provides
//        @ActivityScope
//        fun provideCameraId(): CameraId = CameraId.BACK
//
//    @Provides
//    @ActivityScope
//    fun provideSurfaceView(@ForApplication context: Context): SurfaceView = SurfaceView(context).apply {
//        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//    }

    factory<ICamera> {
        CameraImp(
            get(),
            get(),
            get(),
            get()
        )
    }

    factory { CameraId.BACK }

    factory {
        SurfaceView(get()).apply {
            layoutParams =
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }
}