package com.example.KBA_6_3.presentation.users

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.KBA_6_3.presentation.AuthViewModel
import com.example.KBA_6_3.domain.entity.User
import com.example.KBA_6_3.presentation.login.LoginState

@Composable
fun UsersListScreen(
	navController: NavController,
	viewModel: AuthViewModel,
	onThemeToggle: () -> Unit = {}, // Функция переключения темы
	isDarkTheme: Boolean = false // Текущее состояние темы
) {
	val state by viewModel.usersState.collectAsState()

	when (state) {
		is UsersListState.Loading -> {
			Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
				CircularProgressIndicator()
			}
		}
		is UsersListState.Success -> {
			val users = (state as UsersListState.Success).users
			Column {
				// Добавляем кнопку переключения темы
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(8.dp),
					horizontalArrangement = Arrangement.End,
					verticalAlignment = Alignment.CenterVertically
				) {
					Text("Темная тема")
					Spacer(modifier = Modifier.width(8.dp))
					Switch(
						checked = isDarkTheme,
						onCheckedChange = { onThemeToggle() }
					)
				}

				LazyColumn(
					contentPadding = PaddingValues(8.dp),
					verticalArrangement = Arrangement.spacedBy(8.dp)
				) {
					items(users) { user ->
						UserCard(user) {
							navController.navigate("detail/${user.id}")
						}
					}
				}
			}
		}
		is UsersListState.Error -> {
			Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
				Column(horizontalAlignment = Alignment.CenterHorizontally) {
					Text("Error: ${(state as UsersListState.Error).message}")
					Spacer(modifier = Modifier.height(8.dp))
					Button(onClick = {
						val loginState = viewModel.loginState.value
						val token = (loginState as? LoginState.Success)?.token
						token?.let { viewModel.loadUsers(it) }
					}) {
						Text("Retry")
					}
					Spacer(modifier = Modifier.height(8.dp))
					Button(onClick = { viewModel.logout() }) {
						Text("Logout")
					}
				}
			}
		}
	}
}

@Composable
fun UserCard(user: User, onClick: () -> Unit) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.clickable { onClick() }
	) {
		Row(modifier = Modifier.padding(12.dp)) {
			AsyncImage(
				model = user.image,
				contentDescription = "Avatar",
				modifier = Modifier.size(60.dp),
				contentScale = ContentScale.Crop
			)
			Spacer(modifier = Modifier.width(12.dp))
			Column {
				Text("${user.firstName} ${user.lastName}", style = MaterialTheme.typography.titleMedium)
				Text(user.username, style = MaterialTheme.typography.bodySmall)
				Text(user.email, style = MaterialTheme.typography.bodySmall)
			}
		}
	}
}