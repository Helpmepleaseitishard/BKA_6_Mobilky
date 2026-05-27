package com.example.KBA_6_2.presentation.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.KBA_6_2.domain.entity.Laureate
import com.example.KBA_6_2.presentation.NobelViewModel

@Composable
fun LaureateListScreen(
    navController: NavController,
    viewModel: NobelViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    var selectedYear by remember { mutableStateOf<String?>(null) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var showYearDialog by remember { mutableStateOf(false) }
    var showCategoryDialog by remember { mutableStateOf(false) }

    val categories = listOf(
        "chemistry" to "Chemistry",
        "physics" to "Physics",
        "literature" to "Literature",
        "peace" to "Peace",
        "medicine" to "Medicine",
        "economics" to "Economics"
    )

    val years = (2024 downTo 1901).map { it.toString() }

    fun getCategoryDisplayName(apiName: String?): String {
        return if (apiName == null) "Все категории"
        else categories.find { it.first == apiName }?.second ?: apiName
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { showYearDialog = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(selectedYear ?: "Все годы")
                }

                OutlinedButton(
                    onClick = { showCategoryDialog = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(getCategoryDisplayName(selectedCategory))
                }
            }
        }

        when (state) {
            is LaureateListState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is LaureateListState.Empty -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Лауреаты не найдены")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            selectedYear = null
                            selectedCategory = null
                            viewModel.loadLaureates(year = null, category = null)
                        }) {
                            Text("Сбросить фильтры")
                        }
                    }
                }
            }
            is LaureateListState.Success -> {
                val laureates = (state as LaureateListState.Success).laureates
                if (laureates.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Нет данных")
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(laureates) { laureate ->
                            LaureateCard(laureate) {
                                navController.navigate("detail/${laureate.id}")
                            }
                        }
                    }
                }
            }
            is LaureateListState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Ошибка: ${(state as LaureateListState.Error).message}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.loadLaureates() }) {
                            Text("Повторить")
                        }
                    }
                }
            }
        }
    }

    if (showYearDialog) {
        AlertDialog(
            onDismissRequest = { showYearDialog = false },
            title = { Text("Выберите год") },
            text = {
                Column(
                    modifier = Modifier.heightIn(max = 300.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    TextButton(
                        onClick = {
                            selectedYear = null
                            showYearDialog = false
                            viewModel.loadLaureates(year = null, category = selectedCategory)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Все годы", modifier = Modifier.fillMaxWidth())
                    }
                    Divider()
                    years.forEach { year ->
                        TextButton(
                            onClick = {
                                selectedYear = year
                                showYearDialog = false
                                viewModel.loadLaureates(year = year, category = selectedCategory)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(year, modifier = Modifier.fillMaxWidth())
                        }
                        Divider()
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showYearDialog = false }) {
                    Text("Закрыть")
                }
            }
        )
    }
    
    if (showCategoryDialog) {
        AlertDialog(
            onDismissRequest = { showCategoryDialog = false },
            title = { Text("Выберите категорию") },
            text = {
                Column(
                    modifier = Modifier.heightIn(max = 300.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    TextButton(
                        onClick = {
                            selectedCategory = null
                            showCategoryDialog = false
                            viewModel.loadLaureates(year = selectedYear, category = null)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Все категории", modifier = Modifier.fillMaxWidth())
                    }
                    Divider()
                    categories.forEach { (apiName, displayName) ->
                        TextButton(
                            onClick = {
                                selectedCategory = apiName
                                showCategoryDialog = false
                                viewModel.loadLaureates(year = selectedYear, category = apiName)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(displayName, modifier = Modifier.fillMaxWidth())
                        }
                        Divider()
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCategoryDialog = false }) {
                    Text("Закрыть")
                }
            }
        )
    }
}

@Composable
fun LaureateCard(laureate: Laureate, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = laureate.fullName,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "${laureate.year} - ${laureate.categoryFullName}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = laureate.motivation,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}