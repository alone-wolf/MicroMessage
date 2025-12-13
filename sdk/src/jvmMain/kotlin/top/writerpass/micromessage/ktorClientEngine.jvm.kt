package top.writerpass.micromessage

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO

actual fun ktorClientEngine(): HttpClientEngineFactory<*> {
    return CIO
}