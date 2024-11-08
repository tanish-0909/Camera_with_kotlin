package com.example.mycamera

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mycamera.presentation.CameraScreen
import com.example.mycamera.ui.theme.MyCameraTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if(!arePermissionsGranted()){
            ActivityCompat.requestPermissions(
                this, CAMERA_PERMISSION, 100
            )
        }

        setContent {
            MyCameraTheme {
                CameraScreen(this)
            }
        }
    }

    fun arePermissionsGranted(): Boolean{
        return CAMERA_PERMISSION.all { perssion ->
            ContextCompat.checkSelfPermission(
                applicationContext,
                perssion
            ) == PackageManager.PERMISSION_GRANTED
        }


    }

    companion object {
        val CAMERA_PERMISSION = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO

            )
    }
}


