package com.example.cryptoapi_implementation.di

import com.example.cryptoapi_implementation.data.CoinApi
import com.example.cryptoapi_implementation.data.CoinsRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideCoinApi(moshi: Moshi): CoinApi {
        return Retrofit.Builder()
            .baseUrl("http://felix.somee.com")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(CoinApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCoinRepository(coinApi: CoinApi): CoinsRepository {
        return CoinsRepository(coinApi)
    }
}