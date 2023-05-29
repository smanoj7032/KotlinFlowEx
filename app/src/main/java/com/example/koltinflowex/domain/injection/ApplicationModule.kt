package com.example.koltinflowex.domain.injection

import com.example.koltinflowex.domain.network.api.BaseApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    @Provides
    fun provideUrl(): String = "https://jsonplaceholder.typicode.com/"

    @Provides
    @Singleton
    fun provideRetroFit(baseUrl: String): Retrofit =
        Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    fun provideApiService(retrofit: Retrofit): BaseApi = retrofit.create(BaseApi::class.java)
}