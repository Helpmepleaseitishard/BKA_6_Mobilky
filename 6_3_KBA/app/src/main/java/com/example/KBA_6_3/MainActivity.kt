package com.example.KBA_6_3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.KBA_6_3.presentation.AuthViewModel
import com.example.KBA_6_3.presentation.AuthViewModelFactory
import com.example.KBA_6_3.presentation.detail.UserDetailScreen
import com.example.KBA_6_3.presentation.login.LoginScreen
import com.example.KBA_6_3.presentation.users.UsersListScreen

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			AuthApp()
		}
	}
}

@Composable
fun AuthApp() {
	// Состояние для переключения темы
	var isDarkTheme by remember { mutableStateOf(false) }

	AuthTheme(darkTheme = isDarkTheme) {
		val navController = rememberNavController()
		val context = LocalContext.current.applicationContext
		val viewModel: AuthViewModel = viewModel(
			factory = AuthViewModelFactory(context)
		)

		NavHost(navController = navController, startDestination = "login") {
			composable("login") {
				LoginScreen(navController, viewModel)
			}
			composable("users") {
				UsersListScreen(navController, viewModel,
					onThemeToggle = { isDarkTheme = !isDarkTheme },
					isDarkTheme = isDarkTheme
				)
			}
			composable(
				"detail/{userId}",
				arguments = listOf(navArgument("userId") { type = NavType.IntType })
			) { backStackEntry ->
				val userId = backStackEntry.arguments?.getInt("userId") ?: return@composable
				UserDetailScreen(userId, navController, viewModel)
			}
		}
	}
}

// Компонент для применения темы
@Composable
fun AuthTheme(
	darkTheme: Boolean = isSystemInDarkTheme(),
	content: @Composable () -> Unit
) {
	val colorScheme = if (darkTheme) {
		darkColorScheme(
			primary = darkColorScheme().primary,
			secondary = darkColorScheme().secondary,
		)
	} else {
		lightColorScheme(
			primary = lightColorScheme().primary,
			secondary = lightColorScheme().secondary,
		)
	}

	MaterialTheme(
		colorScheme = colorScheme,
		content = content
	)
}