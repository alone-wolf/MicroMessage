package top.writerpass.micromessage

open class PathNode(
    private val segment: String,
    private val parent: PathNode? = null
) {
    val path: String by lazy {
        val parentPath = parent?.path ?: ""
        if (parentPath.isEmpty()) "/$segment"
        else "$parentPath/$segment"
    }
}

open class ParamPathNode(
    segment: String,
    parent: PathNode? = null
) : PathNode(segment, parent)

object ServerRoutes {

    object Api : PathNode("api") {

        object V1 : PathNode("v1", Api) {

            object Auth : PathNode("auth", V1) {

                object Register : PathNode("register", Auth)
                object Login : PathNode("login", Auth)
                object Logout : PathNode("logout", Auth)
                object Sessions : ParamPathNode("sessions", Auth)
            }

            object User : PathNode("user", V1) {

                object Info : PathNode("info", User)
                object Update : PathNode("update", User)
            }
        }
    }
}
