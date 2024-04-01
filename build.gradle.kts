// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.13"
    id("com.google.gms.google-services") version "4.4.1" apply false
}

buildscript {
    buildscript {
        repositories {
            google()
        }
    }
    dependencies {
        classpath ("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:1.9.0-1.0.13")
        classpath(libs.google.services)
    }
}