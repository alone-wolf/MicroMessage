package top.writerpass.micromessage.core.data.service.friend

import io.ktor.server.auth.principal
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import top.writerpass.micromessage.core.data.base.BaseRouting
import top.writerpass.micromessage.core.data.service.auth.AuthNodes
import top.writerpass.micromessage.core.data.service.auth.principal.UserInfoPrincipal
import top.writerpass.micromessage.core.data.service.friend.data.FriendRelationEntity
import top.writerpass.micromessage.core.data.service.friend.data.FriendRelationTable
import top.writerpass.micromessage.returnOk

object FriendRouting : BaseRouting {
    override fun apiRoutes(route: Route) {
        route.route("/friends") {
            AuthNodes.NormalAccess.run {
                routeWrapper {
                    get{
                        call.principal<UserInfoPrincipal>()?.let { principal ->
                            val userId = principal.userId
                            val friendList = newSuspendedTransaction {
                                FriendRelationEntity.find {
                                    FriendRelationTable.userId eq userId
                                }.toList().map { it.toData() }
                            }
                            call.returnOk(friendList)
                        }
                    }
                }
            }
        }
    }

    override fun adminRoutes(route: Route) {

    }
}