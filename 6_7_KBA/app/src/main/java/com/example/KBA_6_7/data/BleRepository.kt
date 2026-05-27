package com.example.KBA_6_7.data

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

class BleRepository(context: Context) {

	private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
	private val appContext = context.applicationContext

	private val bluetoothManager = appContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
	private val adapter = bluetoothManager.adapter

	private val _devices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
	val devices: StateFlow<List<BluetoothDevice>> = _devices

	private val _heartRate = MutableStateFlow<String?>(null)
	val heartRate: StateFlow<String?> = _heartRate

	private val _connectionState = MutableStateFlow("Disconnected")
	val connectionState: StateFlow<String> = _connectionState

	private var currentGatt: BluetoothGatt? = null

	private val _isScanning = MutableStateFlow(false)
	val isScanning: StateFlow<Boolean> = _isScanning

	private fun hasBlePermissions(): Boolean {
		return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			ActivityCompat.checkSelfPermission(
				appContext, Manifest.permission.BLUETOOTH_SCAN
			) == PackageManager.PERMISSION_GRANTED
		} else {
			ActivityCompat.checkSelfPermission(
				appContext, Manifest.permission.ACCESS_FINE_LOCATION
			) == PackageManager.PERMISSION_GRANTED &&
					ActivityCompat.checkSelfPermission(
						appContext, Manifest.permission.BLUETOOTH
					) == PackageManager.PERMISSION_GRANTED
		}
	}

	private fun hasConnectPermission(): Boolean {
		return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			ActivityCompat.checkSelfPermission(
				appContext, Manifest.permission.BLUETOOTH_CONNECT
			) == PackageManager.PERMISSION_GRANTED
		} else {
			ActivityCompat.checkSelfPermission(
				appContext, Manifest.permission.BLUETOOTH
			) == PackageManager.PERMISSION_GRANTED
		}
	}

	private val scanCallback = object : ScanCallback() {
		override fun onScanResult(callbackType: Int, result: ScanResult) {
			val device = result.device
			val deviceName = try {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
					if (ActivityCompat.checkSelfPermission(appContext, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED)
						device.name else null
				} else device.name
			} catch (e: SecurityException) { null }

			Log.d("BleRepository", "Found: ${deviceName ?: "unnamed"} - ${device.address}")
			if (!deviceName.isNullOrBlank()) {
				val current = _devices.value.toMutableList()
				if (current.none { it.address == device.address }) {
					current.add(device)
					_devices.value = current.toList()
				}
			}
		}

		override fun onScanFailed(errorCode: Int) {
			_isScanning.value = false
			Log.e("BleRepository", "Scan failed: $errorCode")
		}
	}

	fun startScan() {
		if (!adapter.isEnabled) { Log.e("BleRepository", "BT off"); return }
		val scanner = adapter.bluetoothLeScanner ?: return
		if (!hasBlePermissions()) return
		_devices.value = emptyList()
		val settings = ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build()
		try {
			scanner.startScan(null, settings, scanCallback)
			_isScanning.value = true
		} catch (e: SecurityException) { Log.e("BleRepository", "startScan error", e) }
	}

	fun stopScan() {
		if (!hasBlePermissions()) return
		try { adapter.bluetoothLeScanner?.stopScan(scanCallback); _isScanning.value = false }
		catch (e: SecurityException) { }
	}

	fun connect(device: BluetoothDevice) {
		stopScan()
		if (!hasConnectPermission()) return
		try {
			currentGatt = device.connectGatt(appContext, false, gattCallback)
			_connectionState.value = "Connecting"
		} catch (e: SecurityException) { }
	}

	fun disconnect() {
		if (!hasConnectPermission()) return
		try { currentGatt?.disconnect(); currentGatt?.close() }
		catch (e: SecurityException) { }
		finally { currentGatt = null; _connectionState.value = "Disconnected"; _heartRate.value = null }
	}

	private val gattCallback = object : BluetoothGattCallback() {
		override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
			if (newState == BluetoothProfile.STATE_CONNECTED) {
				_connectionState.value = "Connected"
				try { gatt.discoverServices() } catch (e: SecurityException) { }
			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				_connectionState.value = "Disconnected"
				_heartRate.value = null
			}
		}

		override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				val service = gatt.getService(HEART_RATE_SERVICE_UUID)
				if (service == null) { _connectionState.value = "HR service not found"; return }
				val characteristic = service.getCharacteristic(HEART_RATE_MEASUREMENT_UUID)
				if (characteristic == null) { _connectionState.value = "HR char not found"; return }

				try {
					gatt.setCharacteristicNotification(characteristic, true)
					val descriptor = characteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_UUID)
					if (descriptor != null) {
						descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
						gatt.writeDescriptor(descriptor)
					}
				} catch (e: SecurityException) { }
			} else _connectionState.value = "Service discovery failed"
		}

		override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
			parseHeartRate(characteristic.value)
		}

		override fun onDescriptorWrite(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int) {
			Log.d("BleRepository", "Descriptor write: $status")
		}

		private fun parseHeartRate(data: ByteArray?) {
			if (data == null || data.isEmpty()) return
			val flags = data[0].toInt() and 0xFF
			val is16Bit = (flags and 0x01) != 0
			val hrValue = if (is16Bit) {
				if (data.size < 3) return
				((data[2].toInt() and 0xFF) shl 8) or (data[1].toInt() and 0xFF)
			} else {
				if (data.size < 2) return
				data[1].toInt() and 0xFF
			}
			_heartRate.value = "Heart Rate: $hrValue bpm"
		}
	}

	companion object {
		private val HEART_RATE_SERVICE_UUID = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb")
		private val HEART_RATE_MEASUREMENT_UUID = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb")
		private val CLIENT_CHARACTERISTIC_CONFIG_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
	}
}