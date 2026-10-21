plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.smartpantrymanager"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.smartpantrymanager"
        minSdk = 28
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("com.squareup.retrofit2:retrofit:2.11.0")//puts HTTP reqs in ur node.js api
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")//tellls retrofit to convert api's json to javaobjs llike PantrItem and Receipe and back
    implementation("androidx.recyclerview:recyclerview:1.3.2")//the db data for the recipes is in the list in d mem, recycler view knows how to scroll,, adpter tells it wat to show
}