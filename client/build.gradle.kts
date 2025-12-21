plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
//    alias(libs.plugins.android.application)
//    alias(libs.plugins.multiplatform.android.library)
//    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlin.serialization)
}

group = "top.writerpass.micromessage"
version = "1.0.0"

kotlin {
    jvm()
//    wasmJs {
//        browser()
//        binaries.executable()
//    }
//    androidTarget()

//    listOf(
//        iosX64(),
//        iosArm64(),
//        iosSimulatorArm64()
//    ).forEach {
//        it.binaries.framework {
//            baseName = "ComposeApp"
//            isStatic = true
//        }
//    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.materialIconsExtended)
                implementation(compose.animation)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.androidx.navigation.compose)
                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(libs.logger.napier)


                implementation("top.writerpass.libs:KMPLibrary:1.0.0")
                implementation("top.writerpass.libs:CMPLibrary:1.0.0")

                // QRCode Generate
                // https://github.com/alexzhirkevich/qrose
                implementation("io.github.alexzhirkevich:qrose:1.0.1")

                // client
                implementation(ktorLibs.client.core)
                implementation(ktorLibs.client.cio)
                implementation(ktorLibs.client.serialization)
                implementation(ktorLibs.client.contentNegotiation)
                implementation(ktorLibs.client.websockets)
                implementation(ktorLibs.client.auth)
                implementation(ktorLibs.client.logging)
                implementation(ktorLibs.client.encoding)

                implementation(ktorLibs.serialization.kotlinx.json)


                implementation("org.jetbrains.compose.material3.adaptive:adaptive:1.2.0")
                implementation("androidx.compose.material3:material3-adaptive-navigation-suite:1.5.0-alpha11")

                implementation(libs.kotlinx.datetime)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutines.swing)
                implementation(libs.kstore.file)

                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")  // 或最新版本

                implementation(project(":server"))
                implementation(project(":sdk"))
            }
        }
//        val androidMain by getting {
//            dependencies {}
//        }
    }
}

//android {
//    namespace = "top.writerpass.micromessage.client"
//    compileSdk = 36
//
//    defaultConfig {
//        applicationId = "top.writerpass.micromessage.client"
//        minSdk = 24
//        targetSdk = 35
//        versionCode = 1
//        versionName = "1.0"
//
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//    }
//
//    buildTypes {
//        release {
//            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_11
//        targetCompatibility = JavaVersion.VERSION_11
//    }
//}