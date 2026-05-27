// MainActivity.kt
package com.example.KBA_6_6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.KBA_6_6.presentation.detail.LaureateDetailScreen
import com.example.KBA_6_6.presentation.list.LaureateListScreen

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			NobelTheme {
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					NobelApp()
				}
			}
		}
	}
}

@Composable
fun NobelTheme(
	darkTheme: Boolean = isSystemInDarkTheme(),
	content: @Composable () -> Unit
) {
	val colorScheme = if (darkTheme) {
		darkColorScheme()
	} else {
		lightColorScheme()
	}

	MaterialTheme(
		colorScheme = colorScheme,
		content = content
	)
}

@Composable
fun NobelApp() {
	val navController = rememberNavController()
	NavHost(navController = navController, startDestination = "list") {
		composable("list") {
			LaureateListScreen(navController = navController)
		}
		composable(
			"detail/{laureateId}",
			arguments = listOf(navArgument("laureateId") { type = NavType.StringType })
		) { backStackEntry ->
			val laureateId = backStackEntry.arguments?.getString("laureateId") ?: return@composable
			LaureateDetailScreen(laureateId = laureateId, navController = navController)
		}
	}
}