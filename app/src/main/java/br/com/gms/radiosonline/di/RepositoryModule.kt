package br.com.gms.radiosonline.di

import br.com.gms.radiosonline.data.repository.local.LocalRadioStationsRepository
import br.com.gms.radiosonline.data.repository.local.LocalRadioStationsRepositoryImpl
import br.com.gms.radiosonline.data.repository.remote.FirebaseRadioStationsRepositoryImpl
import br.com.gms.radiosonline.data.repository.remote.RemoteRadioStationsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun provideLocalRadioStationsRepository(repository: LocalRadioStationsRepositoryImpl): LocalRadioStationsRepository

    @Binds
    @Singleton
    fun provideRemoteRadioStationsRepository(repository: FirebaseRadioStationsRepositoryImpl): RemoteRadioStationsRepository

}