plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.ksp)

}

android {
    namespace = "com.uv.quotecomposeapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.uv.quotecomposeapp"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {

            // infuture we will make these true(reduce size and increase performance)
            isMinifyEnabled = false
            isShrinkResources = false

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
}

dependencies {

    implementation(libs.androidx.compose.runtime.saveable)
    implementation(libs.androidx.compose.runtime)
    testImplementation("junit:junit:4.13.2")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.4")

    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation("androidx.compose.material:material-icons-extended")
    implementation(libs.androidx.navigation.compose)

    implementation("androidx.compose.runtime:runtime-livedata")

    implementation("com.google.code.gson:gson:2.13.2")

    // Room
    implementation("androidx.room:room-runtime:2.7.0-alpha11")
    implementation(libs.androidx.work.runtime.ktx)
    ksp("androidx.room:room-compiler:2.7.0-alpha11")
    implementation("androidx.room:room-ktx:2.7.0-alpha11")

    // for theme data store
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // work manager dep for notifications
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    // playstore(rating)
    implementation("com.google.android.play:review:2.0.1")
    implementation("com.google.android.play:review-ktx:2.0.1")

    // playstore(check for update)
    implementation("com.google.android.play:app-update:2.1.0")
    implementation("com.google.android.play:app-update-ktx:2.1.0")

    // for splash screen
    implementation("androidx.core:core-splashscreen:1.0.1")

    // for pull to refresh
    implementation("androidx.compose.material:material:1.6.0")


    // Test dependencies
    testImplementation(kotlin("test"))
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}