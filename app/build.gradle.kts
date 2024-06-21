plugins {
    alias(libs.plugins.simplebudget.android.application)
    alias(libs.plugins.simplebudget.android.application.compose)
    alias(libs.plugins.simplebudget.android.hilt)
}

android {
    defaultConfig {
        applicationId = "com.glebkrep.simplebudget"
        versionCode = 3
        versionName = "0.1.2"
        testInstrumentationRunner =
            "com.glebkrep.simplebudget.core.testing.SimpleBudgetTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
    namespace = "com.glebkrep.simplebudget"
}

dependencies {
    implementation(project(":feature:calculator"))
    implementation(project(":feature:preferences"))
    implementation(project(":feature:update-billing"))
    implementation(project(":feature:update-budget"))
    implementation(project(":core:ui"))
    implementation(project(":core:data"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3.adaptive)
    implementation(libs.androidx.compose.material3.adaptive.layout)
    implementation(libs.androidx.compose.material3.adaptive.navigation)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.compose.runtime.tracing)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.splashscreen)
}

