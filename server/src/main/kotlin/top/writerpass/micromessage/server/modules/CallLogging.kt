package top.writerpass.micromessage.server.modules

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.calllogging.CallLogging
import org.slf4j.LoggerFactory
import org.slf4j.event.Level

fun Application.installCallLogging(){
    install(CallLogging) {
        level = Level.DEBUG
        logger = LoggerFactory.getLogger("MicroMessage.Server")

        // 可选：按需求过滤
//                filter { call -> call.request.path() != "/health" }
    }
}