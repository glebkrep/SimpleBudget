plugins {
    alias(libs.plugins.simplebudget.android.feature)
    alias(libs.plugins.simplebudget.android.library.compose)
}

android {
    namespace = "com.glebkrep.simplebudget.feature.update_budget"

}

dependencies {
    implementation(project(":core:domain"))
}