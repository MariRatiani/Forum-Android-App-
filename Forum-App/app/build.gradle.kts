plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)

    //added by me
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.forumapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.forumapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    //added by me
    // Import the Firebase BoM
    // Import the BoM for the Firebase platform
//    implementation(platform("com.google.firebase:firebase-bom:33.1.1"))
    implementation(platform(libs.firebase.bom))
    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
//    implementation("com.google.firebase:firebase-analytics")
    // Declare the dependency for the Cloud Firestore library
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage")
    // When using the BoM, you don't specify versions in Firebase library dependencies

    implementation (libs.firebase.ui.auth)
    implementation (libs.facebook.android.sdk)
    implementation(libs.firebase.auth)

    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries
    implementation(libs.ui)
    implementation (libs.androidx.material3)
    implementation (libs.ui.tooling)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose.android)
    implementation("com.google.firebase:firebase-auth")

    // Also add the dependency for the Google Play services library and specify its version
    implementation(libs.play.services.auth)
    implementation("io.coil-kt:coil-compose:2.6.0")
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.runtime.livedata)

    implementation( libs.androidx.compose.ui.ui.tooling.preview)
    implementation( libs.material3)
    implementation ("androidx.compose.material:material:1.4.0-beta01")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.3")
//    implementation("com.google.accompanist:accompanist-pullrefresh:0.30.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation (libs.firebase.ui.auth)
}