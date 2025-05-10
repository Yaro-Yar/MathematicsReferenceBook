pluginManagement {
    repositories {
        google() // Должен быть первым!
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("com.android.application") version "8.9.2" apply false
        id("org.jetbrains.kotlin.android") version "2.0.0" apply false
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google() // Должен быть первым!
        mavenCentral()
    }
}

rootProject.name = "Mathematics_reference_book"
include(":app")