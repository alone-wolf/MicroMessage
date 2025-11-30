plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlin.serialization)
//    application
}

group = "top.writerpass.micromessage"
version = "1.0.0"


dependencies{
    implementation(libs.kotlinx.datetime)

    implementation("top.writerpass.libs:KMPLibrary:1.0.0")
    implementation("top.writerpass.libs:CMPLibrary:1.0.0")
    implementation("io.github.classgraph:classgraph:4.8.184")
    implementation(ktorLibs.serialization.kotlinx.json)
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.websockets)
    implementation(ktorLibs.server.csrf)
    implementation(ktorLibs.server.partialContent)
    implementation(ktorLibs.server.forwardedHeader)
    implementation(ktorLibs.server.defaultHeaders)
    implementation(ktorLibs.server.cors)
    implementation(ktorLibs.server.compression)
    implementation(ktorLibs.server.auth)
    implementation(ktorLibs.server.auth.jwt)
    implementation(ktorLibs.server.htmlBuilder)
    implementation(libs.kotlinx.html)
    implementation(ktorLibs.server.autoHeadResponse)
    implementation(ktorLibs.server.callLogging)
    implementation(ktorLibs.serialization.kotlinx.json)
    implementation(ktorLibs.server.contentNegotiation)
    implementation(ktorLibs.server.rateLimit)
    implementation(ktorLibs.server.netty)
    implementation(ktorLibs.server.cio)

    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.h2)
    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j)
    implementation(libs.logback)
}
