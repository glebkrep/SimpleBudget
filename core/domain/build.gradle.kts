plugins {
    alias(libs.plugins.simplebudget.android.library)
}

android {
    namespace = "com.glebkrep.simplebudget.core.domain"
}

dependencies {
    api(project(":core:model"))
    implementation(project(":core:data"))
    implementation(libs.javax.inject)
    testImplementation(project(":core:testing"))
    testImplementation(libs.mockk)
}