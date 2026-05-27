package com.example.KBA_6_7.presentation.ui

import android.Manifest
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BleScannerScreen() {
	val context = LocalContext.current
	val viewModel = remember { BleViewModel(context) }

	val devices by viewModel.devices.collectAsState()
	val heartRate by viewModel.heartRate.collectAsState()
	val connectionState by viewModel.connectionState.collectAsState()
	val isScanning by viewModel.isScanning.collectAsState()

	val permissions = remember {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) listOf(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT)
		else listOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION)
	}
	val permissionState = rememberMultiplePermissionsState(permissions)

	LaunchedEffect(permissionState.allPermissionsGranted) {
		if (permissionState.allPermissionsGranted && !isScanning) viewModel.startScan()
	}

	Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
		Text("BLE Heart Rate Monitor", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground)
		Spacer(modifier = Modifier.height(16.dp))

		if (!permissionState.allPermissionsGranted) {
			Text("Требуются разрешения Bluetooth и местоположения", color = MaterialTheme.colorScheme.error)
			Spacer(modifier = Modifier.height(8.dp))
		}

		Button(onClick = {
			if (permissionState.allPermissionsGranted) {
				if (isScanning) viewModel.stopScan() else viewModel.startScan()
			} else permissionState.launchMultiplePermissionRequest()
		}) {
			Text(if (!permissionState.allPermissionsGranted) "Запросить разрешения"
			else if (isScanning) "Остановить" else "Сканировать")
		}

		Spacer(modifier = Modifier.height(16.dp))
		Text("Пульс: ${heartRate ?: "—"}", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
		Text("Статус: $connectionState", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
		Spacer(modifier = Modifier.height(16.dp))

		if (devices.isEmpty() && !isScanning && permissionState.allPermissionsGranted)
			Text("Устройства не найдены", color = MaterialTheme.colorScheme.onBackground)

		LazyColumn {
			items(devices) { device ->
				Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { viewModel.connect(device) },
					colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
					Column(modifier = Modifier.padding(16.dp)) {
						Text(device.name ?: "Без имени", style = MaterialTheme.typography.titleMedium)
						Text(device.address, style = MaterialTheme.typography.bodySmall)
					}
				}
			}
		}

		if (connectionState == "Connected") {
			Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
				Button(onClick = { viewModel.disconnect() }) { Text("Отключиться") }
			}
		}
	}
}