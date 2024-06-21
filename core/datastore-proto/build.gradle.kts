plugins {
    alias(libs.plugins.simplebudget.android.library)
    alias(libs.plugins.protobuf)
}

android {
    namespace = "com.glebkrep.simplebudget.core.datastore.proto"
}

protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                register("java") {
                    option("lite")
                }
                register("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

//androidComponents.beforeVariants { variant ->
//    android.sourceSets.forEach {set ->
//        val buildDir = layout.buildDirectory.get().asFile
//        set.java.srcDir(buildDir.resolve("generated/source/proto/${set.name}/java"))
//        set.kotlin.srcDir(buildDir.resolve("generated/source/proto/${set.name}/kotlin"))
//    }
//}

dependencies{
    api(libs.protobuf.kotlin.lite)
}