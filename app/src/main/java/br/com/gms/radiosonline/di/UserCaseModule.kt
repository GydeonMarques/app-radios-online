package br.com.gms.radiosonline.di

import br.com.gms.radiosonline.data.repository.remote.RemoteRadioStationsRepository
import br.com.gms.radiosonline.domain.usercase.RadioStationsUserCase
import br.com.gms.radiosonline.domain.usercase.RadioStationsUserCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
class UserCaseModule {


    @Provides
    fun provideRadioStationsUserCase(
        @IoDispatcher dispatcher: CoroutineDispatcher,
        remoteRadioStationsRepository: RemoteRadioStationsRepository,
    ): RadioStationsUserCase {
        return RadioStationsUserCaseImpl(
            remoteRadioStationsRepository,
            dispatcher
        )
    }

}