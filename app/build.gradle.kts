plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}


android {
    namespace = "com.example.mathematics_reference_book"
    compileSdk = 35


    defaultConfig {
        applicationId = "com.example.mathematics_reference_book"
        minSdk = 28
        //noinspection OldTargetApi
        targetSdk = 33
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
}


dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Для поиска - используем современную версию SearchView
    implementation(libs.appcompat.resources.v170)


    implementation (libs.appcompat.v7)
    implementation (libs.design)

    implementation (libs.material.v1130alpha13) // Используйте актуальную версию

    implementation (libs.lifecycle.viewmodel.ktx)

    implementation (libs.room.runtime)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.core.ktx)
    implementation(libs.appcompat)

    androidTestImplementation(libs.junit.v115)

    implementation(libs.core.ktx.v1120)
    implementation(libs.appcompat)
    implementation(libs.material)

    // Убедитесь, что все версии существуют
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    implementation (libs.core.ktx)
    implementation (libs.room.runtime)
    annotationProcessor (libs.room.compiler)
    implementation (libs.room.ktx)
    implementation (libs.lifecycle.livedata)
    implementation (libs.annotation)
}