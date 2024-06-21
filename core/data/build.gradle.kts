plugins {
    alias(libs.plugins.simplebudget.android.library)
    alias(libs.plugins.simplebudget.android.hilt)
}

android {
    namespace = "com.glebkrep.simplebudget.core.data"
}

dependencies {
    api(project(":core:common"))
    api(project(":core:datastore"))
    api(project(":core:database"))
}
