plugins {
    alias(libs.plugins.simplebudget.android.library)
    alias(libs.plugins.simplebudget.android.hilt)
}

android {
    namespace = "com.glebkrep.simplebudget.core.datastore"
}

dependencies {
    api(libs.androidx.datastore.preferences)
    implementation(project(":core:common"))
    api(project(":core:model"))
    api(project(":core:datastore-proto"))
}