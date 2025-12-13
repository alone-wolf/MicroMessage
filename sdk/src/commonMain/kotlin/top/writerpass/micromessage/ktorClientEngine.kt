package top.writerpass.micromessage

import io.ktor.client.engine.HttpClientEngineFactory

expect fun ktorClientEngine(): HttpClientEngineFactory<*>