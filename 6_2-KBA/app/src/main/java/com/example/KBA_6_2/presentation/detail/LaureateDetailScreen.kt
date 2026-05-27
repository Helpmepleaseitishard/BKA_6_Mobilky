package com.example.KBA_6_2.presentation.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.KBA_6_2.domain.entity.Laureate
import com.example.KBA_6_2.presentation.NobelViewModel
import com.example.KBA_6_2.presentation.list.LaureateListState

@Composable
fun LaureateDetailScreen(
    laureateId: String,
    navController: NavController,
    viewModel: NobelViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val laureate = if (state is LaureateListState.Success) {
        (state as LaureateListState.Success).laureates.find { it.id == laureateId }
    } else null

    if (laureate == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Laureate not found")
        }
        return
    }

    LaureateDetailContent(laureate)
}

@Composable
fun LaureateDetailContent(laureate: Laureate) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (laureate.portraitUrl != null) {
            Image(
                painter = rememberAsyncImagePainter(laureate.portraitUrl),
                contentDescription = "Portrait of ${laureate.fullName}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )
        }

        Text(laureate.fullName, style = MaterialTheme.typography.headlineSmall)
        Text("${laureate.year} - ${laureate.categoryFullName}", style = MaterialTheme.typography.titleMedium)
        Text("Country: ${laureate.country}", style = MaterialTheme.typography.bodyMedium)

        Divider()

        Text("Motivation:", style = MaterialTheme.typography.titleMedium)
        Text(laureate.fullMotivation, style = MaterialTheme.typography.bodyMedium)
    }
}