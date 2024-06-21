plugins {
    alias(libs.plugins.simplebudget.android.library)
}

android {
    namespace = "com.glebkrep.simplebudget.core.domain"
}

dependencies {
    api(project(":core:data"))
    api(project(":core:model"))
    implementation(libs.javax.inject)
    testImplementation(project(":core:testing"))
    testImplementation(libs.mockk)
}