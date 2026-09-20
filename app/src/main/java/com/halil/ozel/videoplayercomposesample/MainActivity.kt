package com.halil.ozel.videoplayercomposesample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.halil.ozel.videoplayercomposesample.ui.VideoPlayerScreen
import com.halil.ozel.videoplayercomposesample.ui.theme.VideoPlayerComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VideoPlayerComposeTheme {
                VideoPlayerScreen()
            }
        }
    }
}
