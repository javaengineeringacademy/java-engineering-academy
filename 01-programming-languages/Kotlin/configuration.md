# Kotlin Configuration

## build.gradle.kts

Kotlin projects use Gradle with Kotlin DSL for build configuration.

```kotlin
plugins {
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.serialization") version "1.9.0"
}

group = "com.example"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("com.google.code.gson:gson:2.10.1")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
}
```

## Kotlin DSL for Gradle

```kotlin
tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.example.MainKt"
    }
}

kotlin {
    jvmToolchain(17)
}
```

## Compiler Options

```kotlin
tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = listOf(
            "-Xopt-in=kotlin.RequiresOptIn",
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
        )
        allWarningsAsErrors = false
        suppressWarnings = false
    }
}
```

## Kotlin Version Catalog

```toml
# gradle/libs.versions.toml
[versions]
kotlin = "1.9.0"
coroutines = "1.7.3"

[libraries]
kotlin-stdlib = { module = "org.jetbrains.kotlin:kotlin-stdlib", version.ref = "kotlin" }
kotlinx-coroutines = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-core", version.ref = "coroutines" }

[plugins]
kotlin-jvm = { id = "org.jetbrains.kotlin.jvm", version.ref = "kotlin" }
```

## Multiplatform Configuration

```kotlin
kotlin {
    jvm()
    js(IR) {
        browser()
        nodejs()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("com.google.code.gson:gson:2.10.1")
            }
        }
    }
}
```

## Project Properties

```properties
# gradle.properties
kotlin.code.style=official
kotlin.incremental=true
org.gradle.jvmargs=-Xmx2048m
org.gradle.parallel=true
```

## IDE Settings

```kotlin
// .idea/compiler.xml
<component name="Kotlin2JvmCompilerArguments">
    <option name="jvmTarget" value="17" />
</component>
```
