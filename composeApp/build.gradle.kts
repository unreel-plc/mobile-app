import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSymbolicProcessor)
    alias(libs.plugins.ktorfitPlugin)
    alias(libs.plugins.kotlinSerializationPlugin)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.ktor.client.android)
            implementation(libs.ktor.utils.jvm)

            // for splash screen
            implementation("androidx.core:core-splashscreen:1.1.0-alpha02")

            //
            implementation("androidx.camera:camera-core:1.3.4")
            implementation("androidx.camera:camera-camera2:1.3.4")
            implementation("androidx.camera:camera-lifecycle:1.3.4")
            implementation("androidx.camera:camera-view:1.3.4")

            implementation("androidx.credentials:credentials:1.5.0")
            implementation("androidx.credentials:credentials-play-services-auth:1.5.0")
            implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")

            implementation("androidx.work:work-runtime-ktx:2.9.0")

            implementation("io.insert-koin:koin-androidx-workmanager:3.5.6")
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            //implementation("io.ktor:ktor-client-darwin:${ktorVersion}")

        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(libs.coil)
            implementation(libs.coil.network.ktor)
            implementation(libs.coil.compose.core)
            implementation(libs.coil.compose)
            implementation("io.ktor:ktor-client-core:3.1.3")
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.ktorfit)
            implementation(libs.ktor.json)
            implementation(libs.ktor.logging)
            implementation(libs.ktor.negotiation)
            implementation(libs.kotlin.serialization)
            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.composeVM)
            implementation(libs.kotlin.navigation.compose)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)

            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.datastore.preferences)

            implementation("org.orbit-mvi:orbit-core:10.0.0")
            implementation("org.orbit-mvi:orbit-viewmodel:10.0.0")
            implementation("org.orbit-mvi:orbit-compose:10.0.0")

            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
            implementation("io.github.kevinnzou:compose-webview-multiplatform:2.0.2")

            implementation("io.ktor:ktor-client-core:3.1.3")
            implementation("io.ktor:ktor-utils:3.1.3")
            implementation("io.ktor:ktor-io:3.1.3")

            // number formatter
            implementation("com.soywiz.korlibs.korio:korio:4.0.10")
        }
        commonTest.dependencies {
            implementation(libs.koin.test)
            implementation(libs.ktor.mock)
            implementation(libs.kotlin.test)
            implementation(libs.kotlin.coroutine.test)
            implementation(libs.junit)
        }
    }
}

android {
    namespace = "com.unreel.unreel"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.unreel.unreel"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    dependencies {
        debugImplementation(compose.uiTooling)
    }

    packaging {
        resources {
            excludes += "META-INF/kotlinx-io.kotlin_module"
            excludes += "META-INF/atomicfu.kotlin_module"
            excludes += "META-INF/kotlinx-coroutines-io.kotlin_module"
            excludes += "META-INF/kotlinx-coroutines-core.kotlin_module"
        }
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

