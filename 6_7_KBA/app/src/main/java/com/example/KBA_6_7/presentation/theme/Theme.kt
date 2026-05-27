package com.example.KBA_6_7.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
	primary = Color(0xFF6200EE),
	onPrimary = Color.White,
	background = Color.White,
	onBackground = Color.Black,
	surface = Color.White,
	onSurface = Color.Black
)

private val DarkColorScheme = darkColorScheme(
	primary = Color(0xFFBB86FC),
	onPrimary = Color.Black,
	background = Color(0xFF121212),
	onBackground = Color.White,
	surface = Color(0xFF1E1E1E),
	onSurface = Color.White
)

@Composable
fun MyApplicationTheme(
	darkTheme: Boolean = isSystemInDarkTheme(),
	content: @Composable () -> Unit
) {
	val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

	MaterialTheme(
		colorScheme = colorScheme,
		content = content
	)
}