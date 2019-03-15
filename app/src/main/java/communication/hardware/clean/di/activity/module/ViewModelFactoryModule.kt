package communication.hardware.clean.di.activity.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import communication.hardware.clean.ViewModelFactory
import communication.hardware.clean.di.activity.ActivityScope
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
class ViewModelFactoryModule {
    @Provides
    @ActivityScope
    fun provideViewModelFactory(viewModelsMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelProvider.Factory = ViewModelFactory(viewModelsMap)
}