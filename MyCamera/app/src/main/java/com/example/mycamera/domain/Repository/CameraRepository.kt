package com.example.mycamera.domain.Repository

import androidx.camera.view.LifecycleCameraController

interface CameraRepository {
    suspend fun takePhoto(
        controller: LifecycleCameraController
    )
    suspend fun recordVideo(
        controller: LifecycleCameraController
    )
}