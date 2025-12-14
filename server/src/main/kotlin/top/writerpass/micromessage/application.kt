package top.writerpass.micromessage

import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import top.writerpass.micromessage.core.Singletons
import top.writerpass.micromessage.core.data.service.auth.AuthNodes
import top.writerpass.micromessage.server.ServerConfig
import top.writerpass.micromessage.server.ServerContainer

suspend fun applicationServer(){
    Singletons.register.registerTables()
    Singletons.databaseContainer.startWebServer()

    val serverContainer = ServerContainer(
        config = ServerConfig.default,
        extraModules = {
            install(Authentication) {
                AuthNodes.Password.run { install() }
                AuthNodes.RefreshToken.run { install() }
                AuthNodes.NormalAccess.run { install() }
            }
            Singletons.register.registerServiceModules(this)
        }
    )
    serverContainer.startServerSuspend()
}

// DataClass -> 数据结构定义
// Table -> 数据表映射
// Entity -> 数据访问
// Repository -> 数据访问包装
// Service -> 业务逻辑组合
// Route -> 请求接收响应
// ModuleService -> 业务模块注册
// Application -> 应用程序


fun main() {
    Singletons.register.registerTables()
    Singletons.databaseContainer.startWebServer()

    val serverContainer = ServerContainer(
        config = ServerConfig.default,
        extraModules = {
            install(Authentication) {
                AuthNodes.Password.run { install() } 
                AuthNodes.RefreshToken.run { install() }
                AuthNodes.NormalAccess.run { install() }
            }
            Singletons.register.registerServiceModules(this)
        }
    )
    serverContainer.startServer()
}