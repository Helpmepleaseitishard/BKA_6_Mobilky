package com.example.KBA_6_3.presentation.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.KBA_6_3.presentation.AuthViewModel
import com.example.KBA_6_3.presentation.login.LoginState

@Suppress("StateFlowValueCalledInComposition")
@Composable
fun UserDetailScreen(
    userId: Int,
    navController: NavController,
    viewModel: AuthViewModel
) {
    val user by viewModel.userDetailState.collectAsState()
    val token = (viewModel.loginState.value as? LoginState.Success)?.token

    LaunchedEffect(userId, token) {
        if (token != null && user == null) {
            viewModel.loadUserDetail(userId, token)
        }
    }

    if (user == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(user!!.image),
            contentDescription = "Avatar",
            modifier = Modifier.size(150.dp),
            contentScale = ContentScale.Crop
        )
        Text("${user!!.firstName} ${user!!.lastName}", style = MaterialTheme.typography.headlineSmall)
        Text(user!!.username, style = MaterialTheme.typography.titleMedium)
        Text(user!!.email, style = MaterialTheme.typography.bodyLarge)
        Button(
            onClick = {
                viewModel.logout()
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
            }
        ) {
            Text("Logout")
        }
    }
}