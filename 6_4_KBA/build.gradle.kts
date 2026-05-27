plugins {
	kotlin("jvm") version "2.3.21"
	kotlin("plugin.serialization") version "2.3.21"
	application
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
	mavenCentral()
}
application {
	mainClass.set("com.example.ApplicationKt")
}
dependencies {
	implementation("io.ktor:ktor-server-core:2.3.7")
	implementation("io.ktor:ktor-server-netty:2.3.7")
	implementation("io.ktor:ktor-server-content-negotiation:2.3.7")
	implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
	implementation("io.ktor:ktor-server-auth:2.3.7")
	implementation("io.ktor:ktor-server-auth-jwt:2.3.7")
	implementation("io.ktor:ktor-server-call-logging:2.3.7")

	// Logging
	implementation("ch.qos.logback:logback-classic:1.4.14")
	implementation("com.auth0:java-jwt:4.4.0")
	implementation("io.ktor:ktor-server-cors:2.3.7")
	implementation("io.ktor:ktor-server-status-pages:2.3.7")
}

tasks.test {
	useJUnitPlatform()
}