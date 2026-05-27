package com.example.KBA_6_1.presentation.detailscreen

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.KBA_6_1.domain.entity.Photo
import com.example.KBA_6_1.presentation.PhotoCatalogViewModel
import com.example.a6_1.presentation.photoscreen.PhotoListState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

@Composable
fun PhotoDetailScreen(
    photoId: String,
    navController: NavController,
    viewModel: PhotoCatalogViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val photo = if (state is PhotoListState.Success) {
        (state as PhotoListState.Success).photos.find { it.id == photoId }
    } else null

    if (photo == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Photo not found")
        }
        return
    }

    PhotoDetailContent(photo)
}

@Composable
fun PhotoDetailContent(photo: Photo) {
    val context = LocalContext.current
    var isSaving by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val saveLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("image/jpeg")) { uri: Uri? ->
        if (uri != null) {
            scope.launch {
                isSaving = true
                try {
                    saveImageToUri(context, photo.downloadUrl, uri)
                    Toast.makeText(context, "Saved!", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                } finally {
                    isSaving = false
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        AsyncImage(
            model = photo.downloadUrl,
            contentDescription = "Large photo by ${photo.author}",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Author: ${photo.author}", style = MaterialTheme.typography.titleMedium)
        Text("Dimensions: ${photo.width} x ${photo.height}", style = MaterialTheme.typography.bodyMedium)
        Text("URL: ${photo.downloadUrl}", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                saveLauncher.launch("photo_${photo.id}.jpg")
            },
            enabled = !isSaving
        ) {
            if (isSaving) CircularProgressIndicator(modifier = Modifier.size(24.dp))
            else Text("Download")
        }
    }
}

suspend fun saveImageToUri(context: Context, imageUrl: String, uri: Uri) {
    withContext(Dispatchers.IO) {
        val connection = URL(imageUrl).openConnection()
        connection.connect()
        val inputStream = connection.getInputStream()
        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            inputStream.copyTo(outputStream)
        }
        inputStream.close()
    }
}