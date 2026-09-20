package com.halil.ozel.videoplayercomposesample.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player as Media3Player
import androidx.media3.ui.compose.material3.Player
import com.halil.ozel.videoplayercomposesample.R
import com.halil.ozel.videoplayercomposesample.data.VideoItem

@Composable
fun VideoPlayerScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val videoItems by viewModel.videoItems.collectAsStateWithLifecycle()
    val selectedUri by viewModel.selectedUri.collectAsStateWithLifecycle()
    val selectVideoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri -> uri?.let(viewModel::addVideoUri) }
    )

    LifecycleEventEffect(Lifecycle.Event.ON_STOP) {
        viewModel.player.pause()
    }

    VideoPlayerContent(
        videoItems = videoItems,
        selectedUri = selectedUri,
        player = viewModel.player,
        onPickVideo = { selectVideoLauncher.launch(arrayOf("video/*")) },
        onPlayVideo = viewModel::playVideo
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VideoPlayerContent(
    videoItems: List<VideoItem>,
    selectedUri: Uri?,
    player: Media3Player,
    onPickVideo: () -> Unit,
    onPlayVideo: (Uri) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onPickVideo) {
                Icon(
                    imageVector = Icons.Default.VideoLibrary,
                    contentDescription = stringResource(R.string.select_video)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(MaterialTheme.shapes.medium),
                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                tonalElevation = 2.dp
            ) {
                Player(
                    player = player,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.playlist_title),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (videoItems.isEmpty()) {
                EmptyPlaylist(modifier = Modifier.fillMaxSize())
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(bottom = 88.dp)
                ) {
                    items(videoItems, key = { it.contentUri }) { item ->
                        val selected = item.contentUri == selectedUri
                        ListItem(
                            headlineContent = { Text(item.name) },
                            supportingContent = {
                                Text(
                                    text = if (selected) {
                                        stringResource(R.string.now_playing)
                                    } else {
                                        stringResource(R.string.tap_to_play)
                                    }
                                )
                            },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Default.Movie,
                                    contentDescription = null
                                )
                            },
                            colors = ListItemDefaults.colors(
                                containerColor = if (selected) {
                                    MaterialTheme.colorScheme.secondaryContainer
                                } else {
                                    MaterialTheme.colorScheme.surface
                                }
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(MaterialTheme.shapes.medium)
                                .clickable { onPlayVideo(item.contentUri) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyPlaylist(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.VideoLibrary,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.playlist_empty_title),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.playlist_empty_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}
