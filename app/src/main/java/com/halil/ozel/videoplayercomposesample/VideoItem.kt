package com.halil.ozel.videoplayercomposesample

import android.net.Uri
import androidx.media3.common.MediaItem

/**
 * Created by halilozel1903 on 27.01.2024.
 */
data class VideoItem(
    val contentUri: Uri,
    val mediaItem: MediaItem,
    val name: String
)
