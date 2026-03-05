plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // ksp plugin needed by room
    id("com.google.devtools.ksp")
    kotlin("plugin.serialization")
}

android {
    namespace = "edu.virginia.cs.androidapp2"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "edu.virginia.cs.androidapp2"
        minSdk = 27
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

//    kotlinOptions {
//        jvmTarget = "11"
//    }

    // Gemini 3 Pro helped me update this to the new way of defining it since the above string no longer works with certain newer plugins
    // issue and resolution here: https://kotlinlang.org/docs/gradle-compiler-options.html#migrate-from-kotlinoptions-to-compileroptions
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // Retrofit dependencies
    implementation(libs.retrofit)
    implementation(libs.converter.kotlinx.serialization)
    // kotlin serialization for use with retrofit
    implementation(libs.kotlinx.serialization.json)

    // Room dependencies
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Learned how to add this dependency with Gemini 3 Pro
    implementation(libs.androidx.ui.text.google.fonts)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}