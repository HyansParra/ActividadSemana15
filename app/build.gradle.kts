plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.actividadsemana15"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.actividadsemana15"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // LIBRERÍA MQTT CORREGIDA (HANNESA2)
    implementation("com.github.hannesa2:paho.mqtt.android:3.6.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
}