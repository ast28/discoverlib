package com.example.discoverlib.di

import com.example.discoverlib.data.repository.TripRepositoryImpl
import com.example.discoverlib.data.repository.UserRepositoryImpl
import com.example.discoverlib.data.repository.AuthRepositoryImpl
import com.example.discoverlib.domain.TripRepository
import com.example.discoverlib.domain.UserRepository
import com.example.discoverlib.domain.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
}
