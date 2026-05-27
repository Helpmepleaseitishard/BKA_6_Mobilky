plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("org.jetbrains.kotlin.plugin.compose")
	id("org.jetbrains.kotlin.plugin.serialization")
}

android {
	namespace = "com.example.KBA_6_6"
	compileSdk = 36

	defaultConfig {
		applicationId = "com.example.rksmp_pr3_6"
		minSdk = 24
		targetSdk = 36
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	kotlin {
		compilerOptions {
			jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
		}
	}
	buildFeatures {
		compose = true
	}
}

dependencies {

	implementation("androidx.core:core-ktx:1.18.0")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.10.0")
	implementation("androidx.activity:activity-compose:1.13.0")
	implementation(platform("androidx.compose:compose-bom:2026.05.01"))
	implementation("androidx.compose.ui:ui")
	implementation("androidx.compose.ui:ui-graphics")
	implementation("androidx.compose.ui:ui-tooling-preview")
	implementation("androidx.compose.material3:material3")
	testImplementation("junit:junit:4.13.2")
	debugImplementation("androidx.compose.ui:ui-tooling")
	debugImplementation("androidx.compose.ui:ui-test-manifest")
	implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")
	implementation("androidx.navigation:navigation-compose:2.9.8")
	implementation("io.ktor:ktor-client-core:3.5.0")
	implementation("io.ktor:ktor-client-android:3.5.0")
	implementation("io.ktor:ktor-client-content-negotiation:3.5.0")
	implementation("io.ktor:ktor-serialization-kotlinx-json:3.5.0")
	implementation("io.ktor:ktor-client-logging:3.5.0")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
	implementation("io.coil-kt:coil-compose:2.7.0")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.11.0")
	implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")
	implementation("androidx.lifecycle:lifecycle-runtime-compose:2.10.0")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
}