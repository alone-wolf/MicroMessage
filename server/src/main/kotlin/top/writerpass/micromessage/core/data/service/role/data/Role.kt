package top.writerpass.micromessage.core.data.service.role.data

data class UserRole(
    val id:Long,
    val name:String,
    val description:String,
    val permissions:List<String>
)

data class Permission(
    val id:Long,
    val name:String,
    val description:String
)

//