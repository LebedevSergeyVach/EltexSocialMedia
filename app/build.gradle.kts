import com.google.protobuf.gradle.id
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.serialization)
    alias(libs.plugins.protobuf)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
    id("kotlin-parcelize")
}

android {
    namespace = "com.eltex.androidschool"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.eltex.androidschool"
        minSdk = 26 // For LocalDateTime = LocalDateTime.now(),
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // file("secrets.properties")
        val secretsProperties = rootDir.resolve("secrets.properties")
            .bufferedReader()
            .use {
                Properties().apply {
                    load(it)
                }
            }

        buildConfigField("String", "API_KEY", secretsProperties.getProperty("API_KEY"))
        // TODO Registration User
        buildConfigField("String", "AUTHORIZATION", secretsProperties.getProperty("AUTHORIZATION"))
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
        // Для ViewBinding вместо findViewById
        viewBinding = true
        // Для secrets.properties
        buildConfig = true
    }

    // Proto DataStore
    sourceSets {
        getByName("main") {
            java.srcDir("src/main/proto")
        }
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.1"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                id("java") {
                    option("lite")
                }
            }
        }
    }
}

tasks.register("generateProto") {
    dependsOn("generateReleaseProto")
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.ui.tooling.preview.android)
    implementation(libs.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    /*
         Constraintlayout
         https://developer.android.com/reference/androidx/constraintlayout/widget/ConstraintLayout
    */
    implementation(libs.androidx.constraintlayout.v220)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // ViewParticleEmitter ANIM
    implementation(libs.confetti)

    // SplashScreen
    implementation(libs.androidx.core.splashscreen)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // Preferences DataStore Jetpack
    implementation(libs.androidx.datastore.preferences)

    // Proto DataStore Jetpack
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.datastore.core)
    implementation(libs.protobuf.javalite)
    implementation(libs.androidx.datastore.rxjava2)

    /*
        Room SQLite Jetpack
        https://developer.android.com/training/data-storage/room
        https://developer.android.com/jetpack/androidx/releases/room
     */
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    /*
        FragmentActivity
        Navigation Component
        https://developer.android.com/guide/navigation
    */
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // OkHttp
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // SwipeRefreshLayout
    implementation(libs.androidx.swiperefreshlayout)
}
