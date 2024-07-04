import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    java
    id("org.springframework.boot") version "3.3.1"
    id("org.springframework.boot.aot") version "3.3.1" // Optional but provide additional optimizations
    id("io.spring.dependency-management") version "1.1.5"
}

group = "dev.bhuwanupadhyay"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(22)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Add OpenFGA Spring Boot Starter dependency to enable OpenFGA features
    // https://github.com/openfga/spring-boot-starter
    implementation("dev.openfga:openfga-spring-boot-starter:0.0.1")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-logging")
    implementation("org.springframework.boot:spring-boot-starter-web")
    runtimeOnly("com.h2database:h2")
    implementation("org.springframework.boot:spring-boot-starter-security")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.named<BootBuildImage>("bootBuildImage") {
    // Enable CDS and Spring AOT
    environment.put("BP_JVM_CDS_ENABLED", "true")
    environment.put("BP_SPRING_AOT_ENABLED", "true")

    // For multi arch (Apple Silicon) support
    builder.set("paketobuildpacks/builder-jammy-buildpackless-tiny")
    buildpacks.set(listOf("paketobuildpacks/java"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}
