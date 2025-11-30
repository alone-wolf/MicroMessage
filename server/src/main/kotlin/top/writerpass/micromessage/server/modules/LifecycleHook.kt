package top.writerpass.micromessage.server.modules

import io.ktor.server.application.Application
import io.ktor.server.application.install
import top.writerpass.micromessage.server.ServerLifecycleMonitor

fun Application.installLifecycleHook(config: ServerLifecycleMonitor.Configuration) {
    install(ServerLifecycleMonitor.Plugin) {
        onStarting = config.onStarting
        onStarted = config.onStarted
        onStopping = config.onStopping
        onStopped = config.onStopped
    }
}