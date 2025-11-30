package top.writerpass.micromessage.core.data.base

import io.ktor.server.routing.Route

interface BaseRouting {
    fun apiRoutes(route: Route)
    fun adminRoutes(route: Route)
}