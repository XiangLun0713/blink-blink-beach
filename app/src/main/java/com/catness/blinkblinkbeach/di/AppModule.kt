package com.catness.blinkblinkbeach.di

import com.catness.blinkblinkbeach.data.repositories.auth.AuthRepository
import com.catness.blinkblinkbeach.data.repositories.auth.AuthRepositoryImpl
import com.catness.blinkblinkbeach.data.repositories.event.EventRepository
import com.catness.blinkblinkbeach.data.repositories.event.EventRepositoryImpl
import com.catness.blinkblinkbeach.data.repositories.home.HomeRepository
import com.catness.blinkblinkbeach.data.repositories.home.HomeRepositoryImpl
import com.catness.blinkblinkbeach.data.repositories.main.MainRepository
import com.catness.blinkblinkbeach.data.repositories.main.MainRepositoryImpl
import com.catness.blinkblinkbeach.data.repositories.profile.ProfileRepository
import com.catness.blinkblinkbeach.data.repositories.profile.ProfileRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository =
        authRepositoryImpl

    @Provides
    @Singleton
    fun provideMainRepository(mainRepositoryImpl: MainRepositoryImpl): MainRepository =
        mainRepositoryImpl

    @Provides
    @Singleton
    fun provideEventRepository(eventRepositoryImpl: EventRepositoryImpl): EventRepository =
        eventRepositoryImpl

    @Provides
    @Singleton
    fun provideHomeRepository(homeRepositoryImpl: HomeRepositoryImpl): HomeRepository =
        homeRepositoryImpl

    @Provides
    @Singleton
    fun provideProfileRepository(profileRepositoryImpl: ProfileRepositoryImpl): ProfileRepository =
        profileRepositoryImpl
}