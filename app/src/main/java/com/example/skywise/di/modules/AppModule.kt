package com.example.skywise.di.modules

import android.app.Application
import androidx.room.Room
import com.example.skywise.data.apiservices.WeatherApi
import com.example.skywise.data.localsource.LocalSource
import com.example.skywise.data.localsource.LocalSourceImpl
import com.example.skywise.data.remotesource.RemoteSource
import com.example.skywise.data.remotesource.RemoteSourceImpl
import com.example.skywise.data.repository.Repository
import com.example.skywise.data.repository.RepositoryImpl
import com.example.skywise.data.roomdb.RoomClient
import com.example.skywise.data.roomdb.WeatherDatabase
import com.example.skywise.domain.constants.APIKEY
import com.example.skywise.domain.constants.BASE_URL
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    companion object {
        @Provides
        @Singleton
        @Named("APIKEY")
        fun provideApiKey(): String = APIKEY

        @Provides
        @Singleton
        fun provideWeatherApi(): WeatherApi {

            val baseUrl = BASE_URL

            val retrofit: Retrofit =
                Retrofit
                    .Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            return retrofit.create(WeatherApi::class.java)
        }

        @Provides
        @Singleton
        fun provideWeatherDatabase(
            application: Application
        ): WeatherDatabase =
            Room.databaseBuilder(
                application,
                RoomClient::class.java,
                "skywise_database"
            ).build().getWeatherDatabase()
    }


    @Binds
    @Singleton
    abstract fun bindsRepository(repository: RepositoryImpl): Repository


    @Binds
    @Singleton
    abstract fun bindsRemoteSource(remoteSource: RemoteSourceImpl): RemoteSource


    @Binds
    @Singleton
    abstract fun bindsLocalSource(localSource: LocalSourceImpl): LocalSource

}