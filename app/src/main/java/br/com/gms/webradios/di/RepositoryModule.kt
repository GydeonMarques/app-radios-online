package br.com.gms.webradios.di

import br.com.gms.webradios.data.repository.local.LocalRadioStationsRepository
import br.com.gms.webradios.data.repository.local.LocalRadioStationsRepositoryImpl
import br.com.gms.webradios.data.repository.remote.FirebaseRadioStationsRepositoryImpl
import br.com.gms.webradios.data.repository.remote.RemoteRadioStationsRepository
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