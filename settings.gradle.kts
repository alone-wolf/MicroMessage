enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven("https://packages.confluent.io/maven/")
    }
    versionCatalogs {
        create("ktorLibs") {
            from("io.ktor:ktor-version-catalog:3.2.3")
        }
        create("kotlinLibs") {
            from(files("./gradle/KotlinLibs.versions.toml"))
        }
        create("databaseORM") {
            from(files("./gradle/DBORM.versions.toml"))
        }
        create("kotlincrypto") {
            // https://github.com/KotlinCrypto/version-catalog/blob/master/gradle/kotlincrypto.versions.toml
            from("org.kotlincrypto:version-catalog:0.8.0")
        }
    }
}

rootProject.name = "MicroMessage"

includeBuild("../KotlinLibs")
//implementation("top.writerpass.libs:KMPLibrary:1.0.0")
//implementation("top.writerpass.libs:CMPLibrary:1.0.0")

include(":server")
include(":sdk")
include(":client")
