import org.apache.commons.compress.harmony.pack200.PackingUtils.config
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.simplebudget.android.application)
    alias(libs.plugins.simplebudget.android.application.compose)
    alias(libs.plugins.simplebudget.android.hilt)
    alias(libs.plugins.baselineprofile)
}

android {
    defaultConfig {
        applicationId = "com.glebkrep.simplebudget"
        versionCode = 4
        versionName = "0.1.3"
        testInstrumentationRunner =
            "com.glebkrep.simplebudget.core.testing.SimpleBudgetTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }



    signingConfigs {
        create("release") {
            val keystorePropertiesFile = rootProject.file("keystore.properties")
            val keystoreProperties = Properties()
            keystoreProperties.load(FileInputStream(keystorePropertiesFile))
            storeFile = file(keystoreProperties["storeFile"].toString())
            storePassword = keystoreProperties["storePassword"].toString()
            keyAlias = keystoreProperties["keyAlias"].toString()
            keyPassword = keystoreProperties["keyPassword"].toString()
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
                signingConfig = signingConfigs.getByName("release")
            }
            debug {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
                applicationIdSuffix = ".debug"
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

tasks.register("getVersion") {
    doLast {
        println("v" + android.defaultConfig.versionName + "code" + android.defaultConfig.versionCode)
    }
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

    implementation(libs.androidx.profileinstaller)
    "baselineProfile"(project(":baselineprofile"))
}

