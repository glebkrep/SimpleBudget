plugins {
    alias(libs.plugins.simplebudget.android.library)
    alias(libs.plugins.simplebudget.android.hilt)
    alias(libs.plugins.simplebudget.android.library.compose)
}

android {
    namespace = "com.glebkrep.simplebudget.core.testing"
}

dependencies {
    api(kotlin("test"))
    api(libs.androidx.compose.ui.test)
    api(project(":core:data"))
    api(project(":core:model"))

    implementation(libs.androidx.test.rules)
    implementation(libs.hilt.android.testing)
    implementation(libs.kotlinx.coroutines.test)
    implementation(project(":core:common"))
}