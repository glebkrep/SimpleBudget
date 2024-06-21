import com.android.build.gradle.LibraryExtension
import com.glebkrep.simplebudget.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.exclude

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("simplebudget.android.library")
                apply("simplebudget.android.hilt")
            }
            extensions.configure<LibraryExtension> {
                defaultConfig {
                    testInstrumentationRunner =
                        "com.glebkrep.simplebudget.core.testing.SimpleBudgetTestRunner"
                }
                testOptions.animationsDisabled = true
                // android testing
                testOptions.unitTests.isIncludeAndroidResources = true
            }

            // android testing
            target.configurations.configureEach {
                if (this.name == "androidTestRuntimeOnly") {
                    this.exclude(group = "com.google.protobuf", module = "protobuf-lite")
                }
            }


            dependencies {
                add("implementation", project(":core:ui"))

                add("implementation", libs.findLibrary("androidx.hilt.navigation.compose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
                add("implementation", libs.findLibrary("androidx.tracing.ktx").get())
                add("implementation", libs.findLibrary("androidx.compose.runtime.livedata").get())

                // android testing
                add("androidTestImplementation", project(":core:testing"))
                add("androidTestImplementation", libs.findLibrary("kaspresso.main").get())
                add("androidTestImplementation", libs.findLibrary("kaspresso.compose").get())
                add(
                    "androidTestImplementation",
                    libs.findLibrary("androidx.ui.test.junit4.android").get()
                )
                add(
                    "debugImplementation",
                    libs.findLibrary("androidx.compose.ui.test.manifest").get()
                )
            }
        }
    }
}
