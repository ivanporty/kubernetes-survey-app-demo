import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "Example"
version = "0.1.0-SNAPSHOT"

val ktor_version = "1.0.1"

plugins {
  application
  kotlin("jvm") version "1.3.10"
  id("com.google.cloud.tools.jib") version "1.2.0"
}

repositories {
  mavenCentral()
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile>().all {
  kotlinOptions.jvmTarget = "1.8"
}

application {
  mainClassName = "MainKt"
}

dependencies {
  compile(kotlin("stdlib-jdk8"))
  compile("io.ktor:ktor-server-netty:$ktor_version")
  compile("io.ktor:ktor-jackson:$ktor_version")
  compile("ch.qos.logback:logback-classic:1.2.3")
  testCompile(group = "junit", name = "junit", version = "4.12")
}

jib {
  container {
    ports = listOf("8080")
  }
}