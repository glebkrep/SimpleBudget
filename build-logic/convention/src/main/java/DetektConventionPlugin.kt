import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import java.io.File

class DetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("io.gitlab.arturbosch.detekt")
            }
            val rootDir = project.rootDir
            when {
                pluginManager.hasPlugin("com.android.application") -> {
                    val extension = extensions.getByType<DetektExtension>()
                    extension.applicationConfig(rootDir)
                }

                pluginManager.hasPlugin("com.android.library") -> {
                    val extension = extensions.getByType<DetektExtension>()
                    extension.moduleConfig(rootDir)
                }

                else -> {
                    val extension = extensions.getByType<DetektExtension>()
                    extension.moduleConfig(rootDir)
                }
            }
        }
    }
}

fun DetektExtension.applicationConfig(projectDir: File) {
    moduleConfig(projectDir)
}

fun DetektExtension.moduleConfig(projectDir: File) {
    config.setFrom(File("${projectDir.absolutePath}/config/detekt/detekt.yml"))
    reports.xml.required.set(true)
}
