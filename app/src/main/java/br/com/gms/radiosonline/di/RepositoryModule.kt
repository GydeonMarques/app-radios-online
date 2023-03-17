package br.com.gms.radiosonline.di

import br.com.gms.radiosonline.data.repository.remote.FirebaseRadioStationsRepositoryImpl
import br.com.gms.radiosonline.data.repository.remote.RemoteRadioStationsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideFirebaseRadioStationsRepository(): RemoteRadioStationsRepository {
        return FirebaseRadioStationsRepositoryImpl()
    }

}