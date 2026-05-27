package com.example.a6_1.presentation.photoscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.KBA_6_1.domain.entity.Photo
import com.example.KBA_6_1.presentation.PhotoCatalogViewModel

@Composable
fun PhotoListScreen(
    navController: NavController,
    viewModel: PhotoCatalogViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    when (state) {
        is PhotoListState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is PhotoListState.Success -> {
            PhotoGrid(
                photos = (state as PhotoListState.Success).photos,
                onPhotoClick = { photo ->
                    navController.navigate("detail/${photo.id}")
                }
            )
        }
        is PhotoListState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Error: ${(state as PhotoListState.Error).message}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.loadPhotos() }) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}

@Composable
fun PhotoGrid(photos: List<Photo>, onPhotoClick: (Photo) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(photos) { photo ->
            PhotoCard(photo, onPhotoClick)
        }
    }
}

@Composable
fun PhotoCard(photo: Photo, onClick: (Photo) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(photo) }
    ) {
        Column {
            AsyncImage(
                model = photo.thumbnailUrl,
                contentDescription = "Photo by ${photo.author}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = photo.author,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "${photo.width} x ${photo.height}",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}