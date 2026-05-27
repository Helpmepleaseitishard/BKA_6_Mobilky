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
	mainClass.set("com.example.MainKt")
}

dependencies {
	// Ktor (последняя стабильная 3.0.3)
	implementation("io.ktor:ktor-server-core:3.0.3")
	implementation("io.ktor:ktor-server-netty:3.0.3")
	implementation("io.ktor:ktor-server-content-negotiation:3.0.3")
	implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.3")
	implementation("io.ktor:ktor-server-auth:3.0.3")
	implementation("io.ktor:ktor-server-auth-jwt:3.0.3")
	implementation("io.ktor:ktor-server-call-logging:3.0.3")
	implementation("io.ktor:ktor-server-status-pages:3.0.3")

	// Exposed (официальная версия)
	implementation("org.jetbrains.exposed:exposed-core:0.44.1")
	implementation("org.jetbrains.exposed:exposed-dao:0.44.1")
	implementation("org.jetbrains.exposed:exposed-jdbc:0.44.1")
	implementation("org.jetbrains.exposed:exposed-java-time:0.44.1")

	// PostgreSQL
	implementation("com.h2database:h2:2.2.224")
	implementation("org.postgresql:postgresql:42.7.1")
	implementation("com.zaxxer:HikariCP:5.1.0")

	// Security
	implementation("com.auth0:java-jwt:4.4.0")
	implementation("org.mindrot:jbcrypt:0.4")

	// HTTP Client
	implementation("io.ktor:ktor-client-core:3.0.3")
	implementation("io.ktor:ktor-client-cio:3.0.3")
	implementation("io.ktor:ktor-client-content-negotiation:3.0.3")

	// Logging
	implementation("ch.qos.logback:logback-classic:1.4.14")
}

tasks.test {
	useJUnitPlatform()
}