plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.andorid)  // Use the corrected alias here
    alias(libs.plugins.ksp.android)   // Use the correct KSP plugin
    alias(libs.plugins.kotlin.serialize.plugin)
}

android {
    namespace = "com.example.athan"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.athan"
        minSdk = 24
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

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //material icon
    implementation(libs.androidx.material.icons.extended)

    //location
    implementation(libs.play.services.location)

    //galance
    implementation(libs.androidx.glance.appwidget)
    implementation(libs.androidx.glance.material3)

    //hilt
    implementation(libs.android.hilt)
    debugImplementation(libs.android.hilt.test)
    ksp(libs.hilt.comiler)
    implementation(libs.hilt.nav)


    // Ktor dependencies
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.loggin)
    implementation(libs.ktor.auth)
    implementation(libs.ktor.cio)

    //navigation
    implementation(libs.compose.navigation)


    // Kotlinx Serialization JSON
    implementation(libs.kotlinx.serialization.json)


    //room
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    annotationProcessor(libs.room.compiler)
    implementation(libs.room.ktx)
    implementation(libs.room.guava)
    testImplementation(libs.room.testing)


    //qeblah direction
    implementation("io.github.hassaanjamil:qiblaCompass:1.0.0")

    //appcompact
    implementation("androidx.appcompat:appcompat:1.6.1")

}