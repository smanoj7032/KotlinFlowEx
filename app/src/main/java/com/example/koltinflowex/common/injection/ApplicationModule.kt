package com.example.koltinflowex.common.injection

import com.example.koltinflowex.common.network.api.API_KEY
import com.example.koltinflowex.common.network.api.BASE_URL
import com.example.koltinflowex.common.network.api.BaseApi
import com.example.koltinflowex.common.network.api.NETWORK_TIMEOUT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    fun provideUrl() = BASE_URL

    @Provides
    @Singleton
    fun provideConnectionTimeout() = NETWORK_TIMEOUT

    @Provides
    @Singleton
    fun providesOkHttp(): OkHttpClient {
        val requestInterceptor = Interceptor { chain ->
            val url = chain.request().url.newBuilder().addQueryParameter("api_key", API_KEY).build()
            val request = chain.request().newBuilder().url(url).build()
            return@Interceptor chain.proceed(request)
        }
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.apply { loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder().addInterceptor(requestInterceptor)
            .addInterceptor(loggingInterceptor).callTimeout(60, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS).build()
    }


    @Provides
    @Singleton
    fun provideRetroFit(baseUrl: String, client: OkHttpClient): Retrofit =
        Retrofit.Builder().baseUrl(baseUrl).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()

    @Provides
    fun provideApiService(retrofit: Retrofit): BaseApi = retrofit.create(BaseApi::class.java)
}