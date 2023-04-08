package br.com.gms.radiosonline.di

import android.content.Context
import br.com.gms.radiosonline.data.database.RadioStationsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): RadioStationsDatabase {
        return RadioStationsDatabase.newInstance(appContext)
    }
}