plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.example.collagealert"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.collagealert"
        minSdk = 26
        targetSdk = 34
        versionCode = 2
        versionName = "1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        // Get the default debug configuration
        val debugConfig = getByName("debug")

        // Create a release configuration that defaults to debug keys.
        // This avoids build failures when a specific release keystore is not yet provided.
        create("release") {
            storeFile = debugConfig.storeFile
            storePassword = debugConfig.storePassword
            keyAlias = debugConfig.keyAlias
            keyPassword = debugConfig.keyPassword
        }

        // Android Studio may inject an 'externalOverride' signing configuration.
        // We define it here to point to the debug keystore, which fixes the 
        // "Keystore file not found" error if an invalid external path was provided.
        register("externalOverride") {
            storeFile = debugConfig.storeFile
            storePassword = debugConfig.storePassword
            keyAlias = debugConfig.keyAlias
            keyPassword = debugConfig.keyPassword
        }
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            // Use the release signing configuration (which is currently aliased to debug)
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage)

    // Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Glide - image loading
    implementation(libs.glide)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
