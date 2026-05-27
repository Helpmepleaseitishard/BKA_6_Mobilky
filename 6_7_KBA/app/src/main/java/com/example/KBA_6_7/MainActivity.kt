package com.example.KBA_6_7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.KBA_6_7.presentation.theme.MyApplicationTheme
import com.example.KBA_6_7.presentation.ui.BleScannerScreen

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			MyApplicationTheme {
				BleScannerScreen()
			}
		}
	}
}