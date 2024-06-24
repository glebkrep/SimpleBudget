plugins {
    alias(libs.plugins.simplebudget.android.feature)
    alias(libs.plugins.simplebudget.android.library.compose)
}

android {
    namespace = "com.glebkrep.simplebudget.feature.update_billing"
}

dependencies {
    implementation(project(":core:domain"))
}