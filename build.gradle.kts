plugins {
    kotlin("jvm") version "1.4.32"
}

repositories {
    mavenCentral()
}

dependencies {
//    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains:annotations:19.0.0")
    testImplementation("junit:junit:4.12")
}

group = "com.abopu.data"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

val developmentOnly by configurations.creating
configurations {
    runtimeClasspath {
        extendsFrom(developmentOnly)
    }
}