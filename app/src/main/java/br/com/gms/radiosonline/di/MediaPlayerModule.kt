package br.com.gms.radiosonline.di

import android.content.Context
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import java.io.File

@Module
@InstallIn(ServiceComponent::class)
object MediaPlayerModule {

    @Provides
    @ServiceScoped
    fun providePlayerLocalCache(@ApplicationContext context: Context): File {
        return File(context.cacheDir, "media")
    }

    @Provides
    @ServiceScoped
    fun provideDataSourceFactory(@ApplicationContext context: Context): DefaultDataSource.Factory {
        return DefaultDataSource.Factory(context)
    }

    @Provides
    @ServiceScoped
    fun provideCacheDataSourceFactory(
        @ApplicationContext context: Context,
        dataSource: DefaultDataSource.Factory,
        cacheDir: File,
    ): CacheDataSource.Factory {
        val databaseProvider = StandaloneDatabaseProvider(context)
        val cache = SimpleCache(cacheDir, NoOpCacheEvictor(), databaseProvider)

        return CacheDataSource.Factory().apply {
            setCache(cache)
            setUpstreamDataSourceFactory(dataSource)
        }
    }
}