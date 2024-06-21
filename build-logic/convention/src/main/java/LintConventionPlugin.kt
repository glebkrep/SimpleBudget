import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.Lint
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class LintConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            when {
                pluginManager.hasPlugin("com.android.application") ->
                    configure<ApplicationExtension> { lint(Lint::applicationConfig) }

                pluginManager.hasPlugin("com.android.library") ->
                    configure<LibraryExtension> { lint(Lint::moduleConfig) }

                else -> {
                    pluginManager.apply("com.android.lint")
                    configure<Lint>(Lint::moduleConfig)
                }
            }
        }
    }
}

fun Lint.applicationConfig() {
    moduleConfig()
    htmlReport = true
    checkDependencies = true
    checkReleaseBuilds = true
    abortOnError = true
}

fun Lint.moduleConfig() {
    enable += "AppCompatMethod"
    enable += "DuplicateStrings"
    enable += "Registered"
    enable += "StopShip"
    abortOnError = false
}
