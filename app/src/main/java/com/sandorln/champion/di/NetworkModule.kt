package com.sandorln.champion.di

import com.sandorln.champion.BuildConfig
import com.sandorln.champion.network.ChampionService
import com.sandorln.champion.network.VersionService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun providesChampionService(retrofit: Retrofit): ChampionService = retrofit.create(ChampionService::class.java)

    @Provides
    @Singleton
    fun providesVersionService(retrofit: Retrofit): VersionService = retrofit.create(VersionService::class.java)
}