package top.writerpass.micromessage.server

import io.ktor.server.application.*
import io.ktor.server.cio.CIO
import io.ktor.server.engine.*
import top.writerpass.micromessage.server.modules.installCallLogging
import top.writerpass.micromessage.server.modules.installHTTP
import top.writerpass.micromessage.server.modules.installLifecycleHook
import top.writerpass.micromessage.server.modules.installSerialization
import top.writerpass.micromessage.utils.JvmLifecycle

class ServerContainer(
    config: ServerConfig = ServerConfig.default,
    private val extraModules: Application.() -> Unit = {}
): JvmLifecycle {
    val server = embeddedServer(
        factory = CIO,
        host = config.host,
        port = config.port,
        module = {
            installLifecycleHook(config.lifecycleHook)
            installHTTP()
            installSerialization()
            installCallLogging()
            extraModules()
        }
    )

    suspend fun startServerSuspend(){
        server.startSuspend(wait = true)
    }

    suspend fun startServer(){
        server.startSuspend()
    }

    override suspend fun start() {

    }

    override suspend fun stop() {
        TODO("Not yet implemented")
    }
}