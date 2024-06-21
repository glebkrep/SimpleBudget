plugins {
    alias(libs.plugins.simplebudget.android.library)
    alias(libs.plugins.simplebudget.android.library.compose)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    namespace = "com.glebkrep.simplebudget.core.ui"
}

dependencies {
    lintPublish(project(":lint"))

    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.compose.material3)
}
