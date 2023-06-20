pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

rootProject.name = "AtumCore"
include("atum-api")
include("atum-core")
include("atum-plugin")
