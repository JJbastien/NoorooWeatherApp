package com.example.nooroojjbastien.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.nooroojjbastien.utils.WeatherApiStringDetails
import com.example.nooroojjbastien.weatherApiService.WeatherApiNetwork
import com.example.nooroojjbastien.weatherApiService.WeatherApiRepoImplementation
import com.example.nooroojjbastien.weatherApiService.WeatherApiRepository
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class WeatherApiModules {

    @Provides
    fun provideOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    fun provideRetrofit(
        provideOkHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(WeatherApiStringDetails.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(provideOkHttpClient)
            .build()
    }

    @Provides
    fun provideApi(
        retrofit: Retrofit
    ): WeatherApiNetwork {
        return retrofit.create(WeatherApiNetwork::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(
        weatherApi: WeatherApiNetwork,
        dataStore: DataStore<Preferences>
    ): WeatherApiRepository {
        return WeatherApiRepoImplementation(weatherApi, dataStore)
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("settings") }
        )
    }

    // added dispatcher as injection for testing purposes
    @Provides
    @Singleton
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}