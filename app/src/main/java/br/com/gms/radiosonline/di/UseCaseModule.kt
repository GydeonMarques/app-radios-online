package br.com.gms.radiosonline.di

import br.com.gms.radiosonline.domain.usecase.RadioStationsFavoritesUseCase
import br.com.gms.radiosonline.domain.usecase.RadioStationsFavoritesUseCaseImpl
import br.com.gms.radiosonline.domain.usecase.RadioStationsListUseCase
import br.com.gms.radiosonline.domain.usecase.RadioStationsListUseCaseImpl
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