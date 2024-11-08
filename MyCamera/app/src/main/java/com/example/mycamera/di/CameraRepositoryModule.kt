package com.example.mycamera.di

import com.example.mycamera.data.repository.CameraRepositoryImpl
import com.example.mycamera.domain.Repository.CameraRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CameraRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindCameraRepository(
        cameraRepositoryImpl: CameraRepositoryImpl
    ): CameraRepository
}