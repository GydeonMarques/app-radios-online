package br.com.gms.webradios.di

import br.com.gms.webradios.domain.usecase.RadioStationsFavoritesUseCase
import br.com.gms.webradios.domain.usecase.RadioStationsFavoritesUseCaseImpl
import br.com.gms.webradios.domain.usecase.RadioStationsListUseCase
import br.com.gms.webradios.domain.usecase.RadioStationsListUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {

    @Binds
    fun provideRadioStationsUserCase(usecase: RadioStationsListUseCaseImpl): RadioStationsListUseCase

    @Binds
    fun provideRadioStationsFavoritesUserCase(usecase: RadioStationsFavoritesUseCaseImpl): RadioStationsFavoritesUseCase

}