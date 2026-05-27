package com.example.KBA_6_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.a6_1.presentation.photoscreen.PhotoListScreen
import com.example.KBA_6_1.presentation.detailscreen.PhotoDetailScreen
import com.example.KBA_6_1.ui.theme.PhotocatalogTheme

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			PhotocatalogTheme {
				Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
					PhotoCatalogApp()
				}
			}
		}
	}
}

@Composable
fun PhotoCatalogApp() {
	val navController = rememberNavController()
	NavHost(navController = navController, startDestination = "list") {
		composable("list") {
			PhotoListScreen(navController = navController)
		}
		composable(
			"detail/{photoId}",
			arguments = listOf(navArgument("photoId") { type = NavType.StringType })
		) { backStackEntry ->
			val photoId = backStackEntry.arguments?.getString("photoId") ?: return@composable
			PhotoDetailScreen(photoId = photoId, navController = navController)
		}
	}
}