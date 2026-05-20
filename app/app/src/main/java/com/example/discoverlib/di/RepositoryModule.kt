package com.example.discoverlib.di

import com.example.discoverlib.data.remote.api.HotelApiService
import com.example.discoverlib.data.repository.TripRepositoryImpl
import com.example.discoverlib.data.repository.UserRepositoryImpl
import com.example.discoverlib.data.repository.AuthRepositoryImpl
import com.example.discoverlib.data.repository.HotelRepositoryImpl
import com.example.discoverlib.domain.TripRepository
import com.example.discoverlib.domain.UserRepository
import com.example.discoverlib.domain.AuthRepository
import com.example.discoverlib.domain.HotelRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTripRepository(
        tripRepositoryImpl: TripRepositoryImpl
    ): TripRepository

    @Binds
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindHotelRepository(
        hotelRepositoryImpl: HotelRepositoryImpl
    ): HotelRepository
}
