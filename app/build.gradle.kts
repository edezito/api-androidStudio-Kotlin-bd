plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "br.com.estudos.ap1"
    compileSdk = 35

    defaultConfig {
        applicationId = "br.com.estudos.ap1"
        minSdk = 24
        targetSdk = 35
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

    // Configurações de compilação
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    // ATIVAÇÃO DO VIEW BINDING (NOVA SEÇÃO ADICIONADA)
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Bibliotecas básicas
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)


    implementation ("io.coil-kt:coil:1.2.2")
    implementation ("com.google.android.material:material:1.9.0")
}