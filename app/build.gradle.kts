import com.android.build.gradle.internal.component.features.ManifestPlaceholdersCreationConfig

plugins {
    id("com.android.application")
}

android {
    namespace = "com.shaktipumplimted.serviceapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.shaktipumplimted.serviceapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    flavorDimensions += "default"
    productFlavors {
        create("onRole") {
            //flavor configurations here
            applicationIdSuffix = ".onRole"
            dimension = "default"
            buildConfigField ("boolean", "IS_ONROLE", "true")
            resValue ("string", "app_name", "Service App")

        }
        create("offRole") {
            //flavor configurations here
            applicationIdSuffix = ".offRole"
            dimension = "default"
            buildConfigField ("boolean", "IS_ONROLE", "false")
            resValue ("string", "app_name", "Contractual Service App")
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
    buildFeatures {
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}

dependencies {

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    //Fonts
    implementation ("com.intuit.sdp:sdp-android:1.1.0")

    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.5.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.11.0")

    //pull to refresh
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    //Scan QR Code
    implementation ("com.google.android.gms:play-services-code-scanner:16.0.0")
    implementation ("com.journeyapps:zxing-android-embedded:4.1.0")

    //Glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")
}