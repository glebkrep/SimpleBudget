import com.glebkrep.simplebudget.convention.configureKotlinJvm
import org.gradle.api.Plugin
import org.gradle.api.Project

class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
                apply("simplebudget.lint")
                apply("simplebudget.detekt")
            }
            configureKotlinJvm()
        }
    }
}
