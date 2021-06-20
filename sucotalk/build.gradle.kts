import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.0"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.5.0"
	kotlin("plugin.spring") version "1.5.0"
}

group = "com.suco"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-websocket")
	implementation("org.springframework.boot:spring-boot-starter-validation")

	testImplementation("org.springframework.boot:spring-boot-starter-test")

//	implementation ("net.rakugakibox.spring.boot:logback-access-spring-boot-starter:2.7.1")

	implementation ("io.jsonwebtoken:jjwt:0.9.1")
	implementation ("javax.xml.bind:jaxb-api:2.3.0")

	implementation ("com.h2database:h2")

	implementation ("org.webjars.bower:bootstrap:4.3.1")
	implementation ("org.webjars.bower:vue:2.5.16")
	implementation ("org.webjars.bower:axios:0.17.1")
	implementation ("org.webjars:sockjs-client:1.1.2")
	implementation ("org.webjars:stomp-websocket:2.3.3-1")
	implementation ("com.google.code.gson:gson:2.8.0")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
