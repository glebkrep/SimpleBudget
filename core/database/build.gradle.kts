plugins {
    alias(libs.plugins.simplebudget.android.library)
    alias(libs.plugins.simplebudget.android.hilt)
    alias(libs.plugins.simplebudget.android.room)
}

android {
    namespace = "com.glebkrep.simplebudget.core.database"
}

dependencies {
    api(project(":core:model"))

}