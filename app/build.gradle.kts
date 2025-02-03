import com.google.protobuf.gradle.id

import java.io.BufferedReader
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.serialization)
    alias(libs.plugins.protobuf)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
    alias(libs.plugins.daggerHilt)
    id("kotlin-parcelize")
}

android {
    namespace = "com.eltex.androidschool"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.eltex.androidschool"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "v0.20.0 03.02.2025"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // file("secrets.properties")
        val secretsProperties = rootDir.resolve("secrets.properties")
            .bufferedReader()
            .use { buffer: BufferedReader ->
                Properties().apply {
                    load(buffer)
                }
            }

        buildConfigField("String", "API_KEY", secretsProperties.getProperty("API_KEY"))
        // TODO Registration and Login User
        buildConfigField("String", "AUTHORIZATION", secretsProperties.getProperty("AUTHORIZATION"))
        // TODO Get ID registration User
        buildConfigField("Long", "USER_ID", secretsProperties.getProperty("USER_ID"))
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        /**
         * Use Java 8 language features and APIs
         * developer.android.com/studio/write/java8-support
         */
        isCoreLibraryDesugaringEnabled = true
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
    implementation(libs.androidx.foundation.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    /**
     * Constraintlayout
     *  https://developer.android.com/reference/androidx/constraintlayout/widget/ConstraintLayout
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

    /**
     *  Room SQLite Jetpack
     *
     * https://developer.android.com/training/data-storage/room
     * https://developer.android.com/jetpack/androidx/releases/room
     */
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    /**
     * FragmentActivity
     *
     * Navigation Component
     * https://developer.android.com/guide/navigation
     */
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    /**
     * OkHttp
     *
     * https://github.com/square/okhttp
     * https://square.github.io/okhttp/
     */
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    /**
     * Retrofit
     *
     * https://github.com/square/retrofit
     * https://square.github.io/retrofit/
     *
     * https://github.com/JakeWharton/retrofit2-kotlinx-serialization-converter
     * https://github.com/square/retrofit/tree/trunk/retrofit-converters/kotlinx-serialization
     */
    implementation(libs.retrofit)
    implementation(libs.retrofit2.kotlinx.serialization.converter)

    // SwipeRefreshLayout
    implementation(libs.androidx.swiperefreshlayout)

    /**
     * RxJava
     *
     * https://github.com/ReactiveX/RxJava
     * https://reactivex.io/documentation
     */
    implementation(libs.rxjava)
    // Функции для работы с MainThread
    implementation(libs.rxandroid)
    // Adapter для retrofit
    implementation(libs.adapter.rxjava3)
    // Полезные экстеншены для Kotlin
    implementation(libs.rxkotlin)

    /**
     * Use Java 8 language features and APIs
     *
     * developer.android.com/studio/write/java8-support
     */
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    /**
     * Kotlinx coroutines test
     *
     * https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-test/
     * https://developer.android.com/kotlin/coroutines/test
     * https://github.com/Kotlin/kotlinx.coroutines
     */
    testImplementation(libs.kotlinx.coroutines.test)

    /**
     * The perfect companion
     * for your Kotlin journey
     * Inspired by functional, data-oriented
     * and concurrent programming
     *
     * https://arrow-kt.io/learn/quickstart/
     *
     * implementation("io.arrow-kt:arrow-fx-coroutines:1.2.4")
     */
    implementation(libs.arrow.core)

    /**
     * SkeletonLayout
     *
     * https://github.com/Faltenreich/SkeletonLayout
     */
    implementation(libs.skeletonlayout)

    /**
     * Glide
     *
     * https://github.com/bumptech/glide
     */
    implementation(libs.glide)

    /**
     * LeakCanary - проверка на утечки памяти
     *
     * https://github.com/square/leakcanary
     */
    testImplementation(libs.leakcanary.android.test)

    /**
     * DI Dependency injection with Hilt
     *
     * https://developer.android.com/training/dependency-injection/hilt-android#kts
     * https://blog.mindorks.com/dagger-hilt-tutorial/
     */
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
}
