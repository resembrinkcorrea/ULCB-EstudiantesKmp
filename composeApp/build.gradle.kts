import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)

    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.native.cocoapods")
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "ULCB Estudiantes Shared Module"
        homepage = "https://lecordonbleu.pe"
        ios.deploymentTarget = "16.0"
        version = "1.0.0"
        podfile = project.file("../iosApp/Podfile")

        pod("FirebaseCore")
        pod("FirebaseMessaging")
        pod("FirebaseDatabase")

        pod("MSAL") {
            version = "2.7.0"
            extraOpts += listOf("-compiler-option", "-fmodules")
        }

        pod("MercadoPagoDevicesSDK") {
            source = path("${project.projectDir}/local-pods/MercadoPagoDevicesSDK")
        }

        pod("MercadoPagoSDKCoreMethods") {
            source = git("https://github.com/mercadopago/sdk-ios.git") { tag = "1.0.0" }
            moduleName = "CoreMethods"
        }

        pod("MPCoreBridge") {
            source = path("${project.projectDir}/local-pods/MPCoreBridge")
        }

        framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {

        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.lifecycle.process)

            // Ktor - Android engine
            implementation(libs.ktor.client.okhttp)
            implementation(libs.ktor.client.cio)

            // Koin - Android
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)

            implementation("com.google.firebase:firebase-messaging-ktx:24.1.2")
            implementation("com.google.firebase:firebase-database:21.0.0")
            implementation("com.google.android.play:app-update:2.1.0")

            implementation("com.microsoft.identity.client:msal:5.2.0") {
                exclude(group = "com.microsoft.device.display")
            }

            // MercadoPago - device fingerprint para Checkout API
            implementation("com.mercadolibre.android.device:sdk:4.0.1")

            // MercadoPago - Core Methods SDK (tokenización PCI-compliant)
            implementation(project.dependencies.platform("com.mercadopago.android.sdk:sdk-android-bom:0.2.3"))
            implementation("com.mercadopago.android.sdk:core-methods")

        }

        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(compose.material)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            api(compose.materialIconsExtended)

            // Navigation
            implementation(libs.navigation.compose)

            // Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose.viewmodel)

            // Coil
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)

            // Coroutines
            implementation(libs.kotlinx.coroutines.core)

            // DateTime
            implementation(libs.kotlinx.datetime)

            // QR
            implementation(libs.qr.kit)
        }

        iosMain.dependencies {
            // Ktor - iOS engine
            implementation(libs.ktor.client.darwin)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "pe.lecordonbleu.universidadestudiante"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "pe.lecordonbleu.universidadestudiante"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 77
        versionName = "1.7.1"
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
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
}

